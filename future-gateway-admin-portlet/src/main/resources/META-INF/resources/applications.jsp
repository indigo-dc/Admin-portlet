<%@ include file="/init.jsp" %>

<div id="<portlet:namespace />ApplicationsTable">
</div>
 
<aui:script require="future-gateway-admin-portlet/js/fgTable.es">
    var Table = futureGatewayAdminPortletJsFgTableEs.default;
    var columns = ['id', 'name', 'enabled', 'outcome'];
    Liferay.Service(
            '/iam.token/get-token',
            function(obj) {
                var table = new Table('${FGURL}', document.getElementById('<portlet:namespace />ApplicationsTable'), obj);
                table.render('applications', columns, obj);
            }
    );
</aui:script>