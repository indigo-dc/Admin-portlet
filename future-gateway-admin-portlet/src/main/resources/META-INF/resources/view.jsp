<%@ include file="/init.jsp" %>


<aui:nav-bar cssClass="collapse-basic-search" markupView="lexicon">
	<aui:nav cssClass="navbar-nav">
		<portlet:renderURL var="viewUsersFlatURL">
			<portlet:param name="toolbarItem" value="view-all-infra" />
		</portlet:renderURL>

		<aui:nav-item label="user-groups" selected="<%= true %>" />
	</aui:nav>

	<aui:nav-bar-search>
                <aui:form action="<%= "Ciccio" %>" name="searchFm">
                        <liferay-ui:input-search markupView="lexicon" />
                </aui:form>
        </aui:nav-bar-search>
</aui:nav-bar>

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
<aui:script require="future-gateway-admin-portlet/fgTable.es">
	var Logger = futureGatewayAdminPortletFgTableEs.default;

	var loggerOne = new Logger('*** -> ');
	loggerOne.log('Hello');

</aui:script>