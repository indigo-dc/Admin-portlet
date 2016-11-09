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
	    var token = this.token;
	    var tasks = null;
        AUI().use(
                'aui-datatable',
                'aui-datatype',
                'datatable-sort',
                'aui-io-request',
                function(A) {            
            A.io.request(endPoint+'/'+resource, {
                method: 'get',
                dataType: 'json',
                headers: {
                    Authorization: "Bearer " + token
                },
                on: {
                    success: function() {
                        console.log('This is the output of FG' + this.get('responseData'));
                        A.one(realTable).html('');
                        tableManager.dataTable = new A.DataTable({
                            columnset : attributes,
                            recordset : this.get('responseData')[resource],
                        }).render(realTable);
                    },
                    failure: function() {
                        console.log('Non riesco a contattare il FG');
                    }
                }
            });
        });
	}
}

export default FgTable;