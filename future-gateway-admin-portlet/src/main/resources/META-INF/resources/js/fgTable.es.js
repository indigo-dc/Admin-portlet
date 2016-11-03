'use strict';

class FgTable {
	constructor(apiUrl = 'https://localhost/apis/v1.0 ', tableIdentifier='#application') {
		this.apiUrl = apiUrl;
	}
	
	console(msg) {
	    alert(msg);
	}
}

export default FgTable;