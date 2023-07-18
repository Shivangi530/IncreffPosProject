var data = [];
function getInvoiceUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/invoiceReport";
}

function getInvoiceList(){
	var url = getInvoiceUrl();
	console.log("loading....................");
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(response) {
	        //console.log(response);
	        data=response;
	   		displayInvoiceList(response);
	   },
	   error: handleAjaxError
	});
}


//UI DISPLAY METHODS

function displayInvoiceList(data){
	var $tbody = $('#invoice-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.date + '</td>'
		+ '<td>' + e.orderCount + '</td>'
		+ '<td>'  + e.itemCount + '</td>'
		+ '<td>'  + e.revenue + '</td>'
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
	$('#refresh-data').click(getInvoiceList);
}

$(document).ready(init);
$(document).ready(getInvoiceList);
