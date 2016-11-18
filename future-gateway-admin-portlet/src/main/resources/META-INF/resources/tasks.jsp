<%@ include file="/init.jsp" %>

<div id="<portlet:namespace />TasksTable">
</div>
 
<aui:script require="future-gateway-admin-portlet/js/fgTable.es">
    var Table = futureGatewayAdminPortletJsFgTableEs.default;
    var columns = ['id', 'date', 'status', 'description'];
    Liferay.Service(
            '/iam.token/get-token',
            function(obj) {
                var table = new Table('${FGURL}', document.getElementById('<portlet:namespace />TasksTable'), obj);
                table.render('tasks', columns, 'taskDetails', obj);
            }
    );
    
    Liferay.provide(
        window,
        'taskDetails',
        function (id) {
            Liferay.Service(
                '/iam.token/get-token',
                function(obj) {
                    var table = new Table('${FGURL}', document.getElementById('<portlet:namespace />TasksTable'), obj);
                    table.showDetails('tasks', id);
                });
        },
        []);
</aui:script>