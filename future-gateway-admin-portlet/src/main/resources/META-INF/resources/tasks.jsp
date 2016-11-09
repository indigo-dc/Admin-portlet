<%@ include file="/init.jsp" %>

<div id="<portlet:namespace />TasksTable">
    <h2><liferay-ui:message key="fg-tasks"/></h2>
</div>
 
<aui:script require="future-gateway-admin-portlet/js/fgTable.es">
    var Table = futureGatewayAdminPortletJsFgTableEs.default;
    var columns = ['id', 'date', 'status', 'description'];
    Liferay.Service(
            '/iam.token/get-token',
            function(obj) {
                var table = new Table('${FGURL}', '#<portlet:namespace />TasksTable', obj);
                table.render('tasks', columns, obj);
            }
    );
    
</aui:script>