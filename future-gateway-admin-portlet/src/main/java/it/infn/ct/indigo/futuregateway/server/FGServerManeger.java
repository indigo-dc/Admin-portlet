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

import java.util.List;

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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.service.CompanyLocalService;

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
}
