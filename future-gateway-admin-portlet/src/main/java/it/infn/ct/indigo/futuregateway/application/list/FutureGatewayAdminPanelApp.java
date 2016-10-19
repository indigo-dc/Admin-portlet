package it.infn.ct.indigo.futuregateway.application.list;

import it.infn.ct.indigo.futuregateway.constants.FutureGatewayAdminPortletKeys;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.model.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true,
	property = {
		"panel.app.order:Integer=10",
		"panel.category.key=" + PanelCategoryKeys.CONTROL_PANEL_APPS
	},
	service = PanelApp.class
)
public class FutureGatewayAdminPanelApp extends BasePanelApp {

	@Override
	public String getPortletId() {
		return FutureGatewayAdminPortletKeys.FutureGatewayAdmin;
	}

	@Override
	@Reference(
		target = "(javax.portlet.name=" + FutureGatewayAdminPortletKeys.FutureGatewayAdmin + ")",
		unbind = "-"
	)
	public void setPortlet(Portlet portlet) {
		super.setPortlet(portlet);
	}

}