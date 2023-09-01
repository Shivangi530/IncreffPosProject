var table;
var data;
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventoryReport";
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
    table.clear().draw();
    var dataRows = [];
    for (var i in data) {
        var e = data[i];
        console.log(e.barcode);
        var trimmedBrand = e.brand.length > 15 ? e.brand.substring(0, 15) + '...' : e.brand;
        var trimmedCategory = e.category.length > 15 ? e.category.substring(0, 15) + '...' : e.category;
        var row = '<tr>' +
        '<td>' + e.barcode + '</td>' +
        '<td>' + trimmedBrand + '</td>' +
        '<td>' + trimmedCategory + '</td>' +
		'<td>' + e.quantity + '</td>'+
		'</tr>';
        dataRows.push([e.barcode,trimmedBrand, trimmedCategory,e.quantity]);
	}
	table.rows.add(dataRows).draw();
}

function printReport() {
    // Check if data is empty or undefined
    if (!data || data.length === 0) {
        console.error('No data available to generate the report.');
        return;
    }

    var filteredData = data.map(({barcode, brand, category, quantity }) => ({barcode, brand, category, quantity}));

    // Prepare headers and TSV data for the writeFileData function
    var headers = ['Barcode', 'Brand', 'Category','Quantity'];
    var tsvData = filteredData.map(obj => [obj.barcode, obj.brand, obj.category, obj.quantity]);

    // Call the createTsvFile function with the filtered TSV data and file name
    createTsvFile(tsvData, headers, 'InventoryReport.tsv');
}

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getInventoryList);
// Initial data fetch
    getInventoryList();
    table = $('#inventory-table').DataTable({
        columnDefs: [
            { targets: [0, 1, 2, 3], className: "text-center" }
        ],
        searching: true,
        info: true,
        lengthMenu: [
            [5, 10, 20, -1],
            [5, 10, 20, 'All']
        ],
        deferRender: true,
        columns: [
                { searchable: true },
                { searchable: true },
                { searchable: true },
                { searchable: false }
            ],
    });
}

$(document).ready(init);
$(document).ready(getInventoryList);
