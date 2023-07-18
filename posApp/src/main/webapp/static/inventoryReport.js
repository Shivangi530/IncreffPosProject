var data = [];
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}


function getInventoryList(){
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(response) {
	        data=response;
	   		displayInventoryList(response);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayInventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		console.log(e);
		var row = '<tr>'
		//+ '<td>' + e.id + '</td>'
		+ '<td>' + e.brand  + '</td>'
		+ '<td>' + e.category  + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function printReport(fileName) {
     // Check if data is empty or undefined
        if (!data || data.length === 0) {
            console.error('No data available to generate the report.');
            return;
        }
     var headers = Object.keys(data[0]);
     var dataArray = data.map(obj => Object.values(obj));
     var tsvData = [headers, ...dataArray];
     var tsvContent = tsvData.map(row => row.join('\t')).join('\n');

     var blob = new Blob([tsvContent], { type: 'text/tab-separated-values' });
     var url = URL.createObjectURL(blob);

     var link = document.createElement('a');
     link.href = url;
     link.download = fileName;
     link.style.display = 'none';

     document.body.appendChild(link);
     link.click();

     document.body.removeChild(link);
     URL.revokeObjectURL(url);
}

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getInventoryList);
}

$(document).ready(init);
$(document).ready(getInventoryList);
