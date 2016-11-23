<%--
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
--%>
<%@ include file="/init.jsp" %>

<%
String toolbarItem = ParamUtil.getString(renderRequest, "toolbarItem", "view-all-tasks");
%>

<%@ include file="/fragments/toolbar.jspf" %>

<c:choose>
    <c:when test="${param.toolbarItem eq 'view-all-apps'}">
        <liferay-util:include page="/applications.jsp" servletContext="<%= application %>" />
    </c:when>
    <c:when test="${param.toolbarItem eq 'view-all-infras'}">
    <h1>Here should be the Infra list</h1>
    </c:when>
    <c:otherwise>
        <liferay-util:include page="/tasks.jsp" servletContext="<%= application %>" />
    </c:otherwise>
</c:choose>
