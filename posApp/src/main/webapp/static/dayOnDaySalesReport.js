var table;

var data = [];

function getInvoiceUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/invoiceReport";
}

function getInvoiceList(){
	var url = getInvoiceUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(response) {
	        data= response.reverse();
	   		displayInvoiceList(response);
	   },
	   error: handleAjaxError
	});
}


//UI DISPLAY METHODS

function displayInvoiceList(data){
    table.clear().draw();
    var dataRows=[];
    for (var i in data) {
        var e = data[i];
        var formattedDate= formatDate(e.date);
        e.revenue= e.revenue.toFixed(2)
        dataRows.push([formattedDate, e.orderCount, e.itemCount, e.revenue ]);
    }
    table.rows.add(dataRows).draw();
}

function printReport() {
    // Check if data is empty or undefined
    if (!data || data.length === 0) {
        console.error('No data available to generate the report.');
        return;
    }

    var filteredData = data.map(({date,	orderCount,	itemCount,	revenue }) => ({date,	orderCount,	itemCount,	revenue}));

    // Prepare headers and TSV data for the writeFileData function
    var headers = ['Date',	'InvoicedOrdersCount',	'InvoicedItemsCount',	'TotalRevenue'];
    var tsvData = filteredData.map(obj => [ formatDate(obj.date), obj.orderCount, obj.itemCount, obj.revenue]);

    // Call the createTsvFile function with the filtered TSV data and file name
    createTsvFile(tsvData, headers, 'DailySalesReport.tsv');
}

function formatDate(date) {
    const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
    const formattedDate = new Date(date).toLocaleDateString(undefined, options);
    return formattedDate;
}

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getInvoiceList);
	getInvoiceList();
    table = $('#invoice-table').DataTable({
      columnDefs: [
        { targets: [3], orderable: false },
        { targets: [0, 1, 2, 3], className: "text-center" }
      ],
      searching: true,
      info: true,
      lengthMenu: [
        [5, 10, 20, -1],
        [5, 10, 20, 'All']
      ],
      deferRender: true,
    });
}

$(document).ready(init);
