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