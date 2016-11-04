<%@ include file="/init.jsp" %>

<!-- 
<table id="application" class="display" cellspacing="0" width="100%">
    <thead>
        <tr>
            <th>Name</th>
            <th>Position</th>
            <th>Office</th>
            <th>Extn.</th>
            <th>Start date</th>
            <th>Salary</th>
        </tr>
    </thead>
    <tfoot>
        <tr>
            <th>Name</th>
            <th>Position</th>
            <th>ciccio</th>
            <th>Extn.</th>
            <th>Start date</th>
            <th>Ciccio</th>
        </tr>
    </tfoot>
</table>
-->
<div id="<portlet:namespace />TasksTable"></div>    
 
<aui:script require="future-gateway-admin-portlet/js/fgTable.es">
    var Table = futureGatewayAdminPortletJsFgTableEs.default;
    var table = new Table('${FGURL}', '<portlet:namespace />TasksTable');
    var columns = ['id', 'state'];
    
    table.render('tasks');
</aui:script>