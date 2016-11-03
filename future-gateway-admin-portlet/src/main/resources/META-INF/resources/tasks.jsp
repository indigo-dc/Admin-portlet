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

    var table = new Table('*** -> ', '<portlet:namespace />TasksTable');
 
    AUI().use('aui-datatable', function(B) {
    var columns = [
      'name',
      'age'
    ];

    var data = [
      {
        name: 'Bob',
        age: '28'
      },
      {
        name: 'Joe',
        age: '72'
      },
      {
        name: 'Sarah',
        age: '35'
      }
    ];
        var dataTable = new B.DataTable({
            columns : columns,
            data : data,
            scrollable : "y",
        }).render('#<portlet:namespace />TasksTable');
    });
</aui:script>