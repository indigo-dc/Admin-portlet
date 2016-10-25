package it.infn.ct.indigo.futuregateway.portlet.configuration.icon;

import java.util.ResourceBundle;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.configuration.icon.BaseJSPPortletConfigurationIcon;
import com.liferay.portal.kernel.portlet.configuration.icon.PortletConfigurationIcon;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import it.infn.ct.indigo.futuregateway.constants.FutureGatewayAdminPortletKeys;

@Component(
        immediate = true,
        property = {"javax.portlet.name=" + FutureGatewayAdminPortletKeys.FutureGatewayAdmin},
        service = PortletConfigurationIcon.class
)
public class SetServiceconfigurationIcon extends BaseJSPPortletConfigurationIcon {

    @Override
    public boolean isShow(PortletRequest portletRequest) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getJspPath() {
        // TODO Auto-generated method stub
        return "/configuration/icon/update_server.jsp";
    }

    @Override
    public String getMessage(PortletRequest portletRequest) {
        ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
            "content.Language", getLocale(portletRequest), getClass());

        return LanguageUtil.get(resourceBundle, "updpate-fg-server");
    }

    @Override
    public String getURL(
        PortletRequest portletRequest, PortletResponse portletResponse) {

        return "javascript:;";
    }

    @Override
    public double getWeight() {
        return 101;
    }

    @Override
    @Reference(
        target = "(osgi.web.symbolicname=future.gateway.admin.portlet)",
        unbind = "-"
    )
    public void setServletContext(ServletContext servletContext) {
        super.setServletContext(servletContext);
    }
}
