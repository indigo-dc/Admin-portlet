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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import it.infn.ct.indigo.futuregateway.constants.FutureGatewayAdminPortletKeys;
import it.infn.ct.indigo.futuregateway.portlet.FutureGatewayAdminPortlet;
import it.infn.ct.indigo.futuregateway.server.FGServerManeger;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + FutureGatewayAdminPortletKeys.FutureGatewayAdmin,
                "mvc.command.name=/fg/setServer"
                
        },
        service = MVCActionCommand.class
)
public class FGServerSetMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected void doProcessAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
                    throws Exception {
        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.
                getAttribute(WebKeys.THEME_DISPLAY);
        String FGURL= ParamUtil.get(actionRequest, "FGURL", "");
        if (FGURL!= null && !FGURL.isEmpty()) {
            _fgServerManager.setFGUrl(
                    themeDisplay.getCompanyId(), FGURL);
        }
    }

    @Reference(unbind = "-")
    protected void setFGServerManeger(FGServerManeger fgServerManeger) {
            this._fgServerManager = fgServerManeger;
    }

    private Log _log = LogFactoryUtil.getLog(FGServerSetMVCActionCommand.class);
    private FGServerManeger _fgServerManager;
}
