'use strict';
import dom from 'metal-dom/src/dom';
import MultiMap from 'metal-structs/src/MultiMap'
import datatable from 'metal-datatable/src/Datatable';
import ajax from 'metal-ajax/src/Ajax';

class FgTable {
	constructor(apiUrl = 'https://localhost/apis/v1.0 ', tableIdentifier='#application', token='') {
		this.apiUrl = apiUrl;
		this.tableIdentifier = tableIdentifier;
		this.token = token;
	}
	
	render (resource, columns) {
	    var tasks = null;
	    if(this.token.substring(0,4) == 'User' || this.token.substring(0,7) == 'No JSON') {
	        dom.append(this.tableIdentifier,'<h1 class="">No Token available.</h1>');
	        return;
	    }
	    
        var table_data = [{'col1':'test1', 'col2':'test2'},{'col1':'test3', 'col2':'test4'}];
        
        var headers = new MultiMap();
        headers.add('Authorization', 'Bearer ' + this.token);
        headers.add('content-type', 'application/json');
            
        var promise = ajax.request(this.apiUrl+'/'+resource, 'GET',null, headers, null);
        var tableBlock = this.tableIdentifier;        
        promise.then(function(data){
            var tableData = JSON.parse(data.response)[resource];
            tableData.forEach(function(entry){
                Object.keys(entry).forEach(function(keyEntry){
                    if(columns.indexOf(keyEntry) < 0){
                        delete(entry[keyEntry]);
                    }
                    
                });
                columns.forEach(function(keyEntry){
                    entry[keyEntry.capitalize()] = entry[keyEntry];
                    delete(entry[keyEntry]);
                });
            });
            var dt = new datatable({'data': tableData, displayColumnsType: false, formatColumns: unsortColumns}, tableBlock);
        });
	}
}

function unsortColumns(columns) {
    return columns
}

String.prototype.capitalize = function() {
    return this.charAt(0).toUpperCase() + this.slice(1);
}

export default FgTable;