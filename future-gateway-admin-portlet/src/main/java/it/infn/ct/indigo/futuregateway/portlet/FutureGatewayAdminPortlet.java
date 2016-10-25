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
package it.infn.ct.indigo.futuregateway.portlet;

import it.infn.ct.indigo.futuregateway.constants.FutureGatewayAdminPortletKeys;
import it.infn.ct.indigo.futuregateway.server.FGServerManeger;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.css-class-wrapper=futuregateway_admin-portlet",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.layout-cacheable=true",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=FutureGateway",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + FutureGatewayAdminPortletKeys.FutureGatewayAdmin,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.supports.mime-type=text/html"
	},
	service = Portlet.class
)
public class FutureGatewayAdminPortlet extends MVCPortlet {

	@Override
	protected void doDispatch(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		try{
			String fgURL = _fgServerManager.getFGUrl(themeDisplay.getCompanyId());
            renderRequest.setAttribute("FGURL", fgURL);
	        if (fgURL == null || fgURL.isEmpty()) {
	            include("/setup.jsp", renderRequest, renderResponse);
	            return;
	        }
		} catch (PortalException pe) {
			_log.info("The FutureGateway URL cannot be retrieved");
		}
		super.doDispatch(renderRequest, renderResponse);
	}

	@Reference(unbind = "-")
    protected void setFGServerManeger(FGServerManeger fgServerManeger) {
            this._fgServerManager = fgServerManeger;
    }

	private Log _log = LogFactoryUtil.getLog(FutureGatewayAdminPortlet.class);
	private FGServerManeger _fgServerManager;
}