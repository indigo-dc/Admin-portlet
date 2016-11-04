'use strict';

class FgTable {
	constructor(apiUrl = 'https://localhost/apis/v1.0 ', tableIdentifier='#application', token='') {
		this.apiUrl = apiUrl;
		this.tableIdentifier = tableIdentifier;
		this.token = token;
	}
	
	console(msg) {
	    alert(msg);
	}
	
	render (resource, attributes) {
	    var realTable = this.tableIdentifier;
	    var endPoint = this.apiUrl;
	    var tableManager = this;
	    var tasks = null;
        AUI().use('aui-datatable', 'aui-io-request', function(A) {            
            A.io.request(endPoint+'/'+resource, {
                method: 'get',
                dataType: 'json',
                on: {
                    success: function() {
                        tableManager.dataTable = new A.DataTable({
                            columnset : attributes,
                            recordset : this.get('responseData'),
                        }).render("#"+realTable);
                    }
                }
            });
        });
	    
	}
}

export default FgTable;