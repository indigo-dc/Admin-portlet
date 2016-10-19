<%@page import="com.liferay.portal.kernel.util.PortalUtil"%>
<%@ include file="/init.jsp" %>

<div class="authorize">
        <div class="title">
        <div class="left-block title-logo"><img class="title-logo" src="<%= PortalUtil.getPathContext(request) %>/images/logo.png" /></div>
        <div class="title-block"><span><liferay-ui:message key="liferay-futuregateway-component" /></span></div>
        </div>
        

        <p>
                <liferay-ui:message key="liferay-futuregateway-configuration" />
        </p>
        <aui:f<aui:form action="action" method="post" name="name">

        </aui:form>

        <aui:button onClick="<%= "Ciao" %>" value="sign-in" />
</div>
