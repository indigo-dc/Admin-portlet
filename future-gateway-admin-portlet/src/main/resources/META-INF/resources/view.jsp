<%@ include file="/init.jsp" %>

<%
String toolbarItem = ParamUtil.getString(renderRequest, "toolbarItem", "view-all-tasks");
%>

<%@ include file="/fragments/toolbar.jspf" %>

<c:choose>
    <c:when test="${param.toolbarItem eq 'view-all-apps'}">
    <h1>Here should be the App list</h1>
    </c:when>
    <c:when test="${param.toolbarItem eq 'view-all-infras'}">
    <h1>Here should be the Infra list</h1>
    </c:when>
    <c:otherwise>
        <liferay-util:include page="/tasks.jsp" servletContext="<%= application %>" />
    </c:otherwise>
</c:choose>
