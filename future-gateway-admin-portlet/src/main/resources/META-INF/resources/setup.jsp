<%@ include file="/init.jsp"%>

<div class="authorize">
 <div class="title">
  <div class="left-block title-logo">
   <img class="title-logo" src="<%= PortalUtil.getPathContext(request) %>/images/logo.png" />
  </div>
  <div class="title-block">
   <span><liferay-ui:message key="liferay-futuregateway-component" /></span>
  </div>
 </div>


 <p>
  <liferay-ui:message key="liferay-futuregateway-configuration" />
 </p>


 <portlet:actionURL name="/fg/setServer" var="setServerURL"
  secure="<%= PropsValues.COMPANY_SECURITY_AUTH_REQUIRES_HTTPS || request.isSecure() %>">
 </portlet:actionURL>

 <aui:form action="<%= setServerURL %>" method="post" name="name">
  <aui:fieldset>
   <aui:input name="FGURL" type="text" value="${FGURL}" label="fg-url">
    <aui:validator name="required" />
    <aui:validator name="url" />
   </aui:input>
  </aui:fieldset>
  <aui:button-row>
   <aui:button cssClass="btn-lg" type="submit" />
  </aui:button-row>

 </aui:form>

</div>
