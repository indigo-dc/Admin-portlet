/**
 * *********************************************************************
 * Copyright (c) 2016: Istituto Nazionale di Fisica Nucleare (INFN) -
 * INDIGO-DataCloud
 *
 * See http://www.infn.it and and http://www.consorzio-cometa.it for details on
 * the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **********************************************************************
 */
package it.infn.ct.indigo.futuregateway.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.expando.kernel.exception.DuplicateColumnNameException;
import com.liferay.expando.kernel.exception.DuplicateTableNameException;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.security.sso.iam.IAM;

import it.infn.ct.indigo.futuregateway.constants.FutureGatewayAdminPortletKeys;

/**
 * Manager of the Future Gateway instance.
 * The manager is responsible to keep the information of the Future Gateway
 * instance to use in the portal and the basic interaction during the
 * administration.
 *
 * <i>Note: the class is responsible for the interaction server side,
 * other interactions will occure on the client side by the javascript
 * part of the portlet.</i>
 */
@Component(immediate = true, service = FGServerManeger.class)
public class FGServerManeger {

    /**
     * Sets the FG end point for the portal instance.
     *
     * @param companyId The id of the instance
     * @param url The URL of the FG
     * @throws PortalException If the value cannot be saved
     */
    public final void setFGUrl(final long companyId, final String url)
            throws PortalException {
        expandoValueService.addValue(
                companyId, FGServerManeger.class.getName(), "FG", "fgUrl",
                0, url);
    }

    /**
     * Retrieves the FG end point for the portal instance.
     *
     * @param companyId The id of the instance
     * @return The URL of the FG
     * @throws PortalException If the values cannot be accessed
     */
    public final String getFGUrl(final long companyId)
            throws PortalException {
        return expandoValueService.getData(companyId,
                FGServerManeger.class.getName(), "FG", "fgUrl", 0, "");
    }

    /**
     * Add a new resource into the FG service.
     *
     * @param companyId The id of the instance
     * @param collection The name of the collection to post the new resource
     * @param resource The resource information in JSON format
     * @throws PortalException Cannot retrieve the server endpoint
     * @throws IOException Connect communicate with the server
     */
    public final void addResource(final long companyId,
            final String collection, final String resource)
                    throws PortalException, IOException {
        log.debug("Adding the new application: " + resource);
        URL url = new URL(getFGUrl(companyId) + "/" + collection);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        OutputStream os = connection.getOutputStream();
        os.write(resource.getBytes());
        os.flush();
        if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new IOException("Server response with code: "
                    + connection.getResponseCode());
        }
        connection.disconnect();
    }

    /**
     * Retrieves a collection from the FG service.
     *
     * @param companyId The id of the instance
     * @param collection The name of the collection to post the new resource
     * @param token The authorisation token for the user
     * @return The full raw collection in json format
     * @throws PortalException Cannot retrieve the server endpoint
     * @throws IOException Connect communicate with the server
     */
    public final String getCollection(final long companyId,
            final String collection, final String token)
                    throws PortalException, IOException {
        URL url = new URL(getFGUrl(companyId) + "/" + collection);
        log.debug("Get the collection from " + url.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type",
                FutureGatewayAdminPortletKeys.FUTURE_GATEWAY_CONTENT_TYPE);
        connection.setRequestProperty("Authorization",
                "Bearer " + token);
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            connection.disconnect();
            log.debug("FG server response code not correct: "
                    + connection.getResponseCode());
            throw new IOException("Server response with code: "
                    + connection.getResponseCode());
        }
        BufferedReader collIn = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder collections = new StringBuilder();
        while ((inputLine = collIn.readLine()) != null) {
            collections.append(inputLine);
        }
        collIn.close();
        log.debug("This is the collection: " + collections.toString());
        return collections.toString();
    }

    /**
     * Retrieves the available infrastructures from the FG service.
     *
     * @param companyId The id of the instance
     * @param userId The id of the user performing the request
     * @return A map of enabled infrastructures. The map contains id and name
     * @throws Exception Cannot communicate with the server
     */
    public final Map<String, String> getInfrastructures(
            final long companyId, final long userId)
                    throws Exception {
        Map<String, String> mapInfras = new HashMap<>();
        String rawCollection = getCollection(companyId,
                FutureGatewayAdminPortletKeys.
                    FUTURE_GATEWAY_INFRASTRUCTURE_COLLECTION,
                iam.getUserToken(userId));
        JSONObject jsonInfras =
                JSONFactoryUtil.createJSONObject(rawCollection);
        log.debug("Infrastructure json: " + jsonInfras.toJSONString());
        JSONArray jAInfras = jsonInfras.getJSONArray(
                FutureGatewayAdminPortletKeys.
                    FUTURE_GATEWAY_INFRASTRUCTURE_COLLECTION);
        log.debug("Available " + jAInfras.length() + "infrastructures");
        for (int i = 0; i < jAInfras.length(); i++) {
            JSONObject jOInfra = jAInfras.getJSONObject(i);
            log.debug("Infrastructure "
                    + jOInfra.getString(FutureGatewayAdminPortletKeys.
                                FUTURE_GATEWAY_ATTRIBUTE_ID)
                    + " enabled option is "
                    + jOInfra.getBoolean(FutureGatewayAdminPortletKeys.
                            FUTURE_GATEWAY_ATTRIBUTE_ENABLED));
            if (jOInfra.getBoolean(FutureGatewayAdminPortletKeys.
                    FUTURE_GATEWAY_ATTRIBUTE_ENABLED)) {
                mapInfras.put(
                        jOInfra.getString(FutureGatewayAdminPortletKeys.
                                FUTURE_GATEWAY_ATTRIBUTE_ID),
                        jOInfra.getString(FutureGatewayAdminPortletKeys.
                                FUTURE_GATEWAY_ATTRIBUTE_NAME));
            }
        }
        return mapInfras;
    }

    /**
     * Actions to do when the component is activated.
     * The only activity is to create the tables in the DB to save
     * the configurations for the FG
     */
    @Activate
    protected final void activate() {
        List<Company> companys = companyService.getCompanies();

        for (Company company : companys) {
            try {
                setupExpando(company.getCompanyId());
            } catch (Exception e) {
                if (log.isWarnEnabled()) {
                    log.warn("Unable to setup FutureGateway service for"
                            + " company " + company.getCompanyId() + ": "
                            + e.getMessage());
                }
            }
        }
    }

    /**
     * Sets the company service.
     * Used to retrieve the list of instances during the activation
     *
     * @param companyLocalService The local company service
     */
    @Reference(unbind = "-")
    protected final void setCompanyLocalService(
            final CompanyLocalService companyLocalService) {
        companyService = companyLocalService;
    }

    /**
     * Sets the ExpandoColumn service.
     * Used to add columns in the expando table related to the FG
     *
     * @param expandoColumnLocalService The local expando column service
     */
    @Reference(unbind = "-")
    protected final void setExpandoColumnLocalService(
            final ExpandoColumnLocalService expandoColumnLocalService) {
        expandoColumnService = expandoColumnLocalService;
    }

    /**
     * Sets the ExpandoTable service.
     * Used to create an expando table for the FG
     *
     * @param expandoTableLocalService The local expando table service
     */
    @Reference(unbind = "-")
    protected final void setExpandoTableLocalService(
            final ExpandoTableLocalService expandoTableLocalService) {
        expandoTableService = expandoTableLocalService;
    }

    /**
     * Sets the ExpandoValue service.
     * Used to save and retrive values from the expando table
     *
     * @param expandoValueLocalService The local expando value service
     */
    @Reference(unbind = "-")
    protected final void setExpandoValueLocalService(
            final ExpandoValueLocalService expandoValueLocalService) {
        expandoValueService = expandoValueLocalService;
    }

    /**
     * Sets the ModuleServiceLifecycle.
     * <b>Not used</b>
     * @param moduleServiceLifecycle The module service lifecycle
     */
    @Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
    protected final void setModuleServiceLifecycle(
            final ModuleServiceLifecycle moduleServiceLifecycle) {
    }

    /**
     * Creates the expando table for the FG.
     *
     * @param companyId The id of the instance owning the table
     * @throws Exception If the table cannot be created
     */
    protected final void setupExpando(final long companyId) throws Exception {
        ExpandoTable table = null;
        try {
            table = expandoTableService.addTable(
                    companyId, FGServerManeger.class.getName(), "FG");
        } catch (DuplicateTableNameException dtne) {
            table = expandoTableService.getTable(
                    companyId, FGServerManeger.class.getName(), "FG");
        }

        try {
            expandoColumnService.addColumn(
                    table.getTableId(), "fgUrl", ExpandoColumnConstants.STRING);
        } catch (DuplicateColumnNameException dcne) {
        }
    }

    /**
     * Sets the iam component.
     *
     * @param iamComp The iam component
     */
    @Reference(unbind = "-")
    protected final void setIam(final IAM iamComp) {
        this.iam = iamComp;
    }

    /**
     * The logger.
     */
    private final Log log = LogFactoryUtil.getLog(FGServerManeger.class);

    /**
     * The Company service.
     */
    private CompanyLocalService companyService;

    /**
     * The ExpandoColumn service.
     */
    private ExpandoColumnLocalService expandoColumnService;

    /**
     * The ExpandoTable service.
     */
    private ExpandoTableLocalService expandoTableService;

    /**
     * The ExpandoValue service.
     */
    private ExpandoValueLocalService expandoValueService;

    /**
     * The IAM service.
     */
    private IAM iam;
}
