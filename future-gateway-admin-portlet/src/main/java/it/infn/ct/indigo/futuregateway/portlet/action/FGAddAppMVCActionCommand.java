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
package it.infn.ct.indigo.futuregateway.portlet.action;

import java.io.IOException;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import it.infn.ct.indigo.futuregateway.constants.FutureGatewayAdminPortletKeys;
import it.infn.ct.indigo.futuregateway.server.FGServerManeger;

/**
 * Implementation of the add application in FG server action command.
 */
@Component(
        immediate = true,
        property = {
                "javax.portlet.name="
                        + FutureGatewayAdminPortletKeys.FUTURE_GATEWAY_ADMIN,
                "mvc.command.name=/fg/addApp"
        },
        service = MVCActionCommand.class
)
public class FGAddAppMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected final void doProcessAction(final ActionRequest actionRequest,
            final ActionResponse actionResponse) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(
            WebKeys.THEME_DISPLAY);

        String redirect = PortalUtil.escapeRedirect(
                ParamUtil.getString(actionRequest, "redirect"));
        String name = ParamUtil.getString(actionRequest, "fg-app-name");
        String description = ParamUtil.getString(actionRequest,
                "fg-app-description");
        boolean enabled = ParamUtil.getBoolean(actionRequest, "fg-app-enabled");
        String outcome = ParamUtil.getString(actionRequest, "fg-app-outcome");
        String[] paramNames = ParamUtil.getStringValues(
                actionRequest, "fg-app-parameter-name");
        String[] paramValues = ParamUtil.getStringValues(
                actionRequest, "fg-app-parameter-value");
        String[] paramDescriptions = ParamUtil.getStringValues(
                actionRequest, "fg-app-parameter-description");
        String[] infras = ParamUtil.getStringValues(actionRequest,
                "fg-app-infrastructure");

        JSONObject jsonApp = JSONFactoryUtil.createJSONObject();
        jsonApp.put("name", name);
        jsonApp.put("description", description);
        jsonApp.put("enabled", enabled);
        jsonApp.put("outcome", outcome);
        try {
            fgServerManager.addResource(
                    themeDisplay.getCompanyId(),
                    FutureGatewayAdminPortletKeys.
                        FUTURE_GATEWAY_APPLICATION_COLLECTION,
                    jsonApp.toJSONString(), themeDisplay.getUserId());
            sendRedirect(actionRequest, actionResponse, redirect);
        } catch (IOException io) {
            log.error(io.getMessage());
            SessionErrors.add(actionRequest, io.getClass(), io);
            try {
                Map<String, String> mapInfras =
                        fgServerManager.getInfrastructures(
                        themeDisplay.getCompanyId(), themeDisplay.getUserId());
                actionRequest.setAttribute(
                        FutureGatewayAdminPortletKeys.
                            FUTURE_GATEWAY_INFRASTRUCTURE_COLLECTION,
                            mapInfras
                        );
            } catch (Exception e) {
                if (e instanceof PrincipalException) {
                    SessionErrors.add(actionRequest, e.getClass());
                    actionResponse.setRenderParameter(
                            "mvcPath", "/error.jsp");
                } else {
                    throw new PortletException(e);
                }
            }
        }
        actionResponse.setRenderParameter(
                "mvcPath", "/add_application.jsp");
    }

    /**
     * Sets the FG Server manager.
     * This is used to get information of the service and for interactions.
     *
     * @param fgServerManeger The FG Server manager
     */
    @Reference(unbind = "-")
    protected final void setFGServerManeger(
            final FGServerManeger fgServerManeger) {
        this.fgServerManager = fgServerManeger;
    }

    /**
     * The logger.
     */
    private Log log = LogFactoryUtil.getLog(FGAddAppMVCRenderCommand.class);

    /**
     * The reference to the FG Server manager.
     */
    private FGServerManeger fgServerManager;

}
