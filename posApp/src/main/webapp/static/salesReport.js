var table;

var salesData = [];
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/salesReport";
}

function getOrderListByFilter(){
	var url = getOrderUrl();
	var $form = $("#orderItem-form");
    var json1 = toJson($form);
    var json = JSON.parse(json1);

    const startDate = new Date(json.startDate);
    const endDate = new Date(json.endDate);

    if (startDate > endDate) {
      danger("startDate:"+startDate.toISOString().split("T")[0]+" cannot be later than endDate:"+endDate.toISOString().split("T")[0]);
      return;
    }
    if(json.startDate===""){
      var currentDate = new Date();
      var oneMonthAgo = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, currentDate.getDate());
      var formattedDate = oneMonthAgo.toISOString().split('T')[0];
        document.getElementById("inputStartDate").value=formattedDate;
        json.startDate=formattedDate;
    }
    if(json.endDate===""){
          var currentDate = new Date();
          var formattedDate = currentDate.toISOString().split('T')[0];
            document.getElementById("inputEndDate").value=formattedDate;
            json.endDate=formattedDate;
        }
    console.log("json is:",json);
    $.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: JSON.stringify(json),
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	        salesData=response;
    	        displayOrderList(response);
    	        findBrandCategory();
    	   },
    	   error: handleAjaxError
    	});
}

//UI DISPLAY METHODS

function displayOrderList(data){
    table.clear().draw();
    var dataRows=[];
    for (var i in data) {
        var e = data[i];
        var trimmedBrand = e.brand.length > 15 ? e.brand.substring(0, 15) + '...' : e.brand;
        var trimmedCategory = e.category.length > 15 ? e.category.substring(0, 15) + '...' : e.category;
        dataRows.push([ trimmedBrand, trimmedCategory, e.quantity,parseFloat(e.revenue).toFixed(2)]);
    }
    table.rows.add(dataRows).draw();
}

function printReport() {
    // Check if data is empty or undefined
    if (!salesData || salesData.length === 0) {
        console.error('No data available to generate the report.');
        return;
    }
    var filteredData = salesData.map(({	brand,	category,	quantity,	revenue }) => ({	brand,	category,	quantity,	revenue}));
    // Headers and TSV data for the writeFileData function
    var headers = [	'Brand',	'Category',	'Quantity',	'Revenue'];
    var tsvData = filteredData.map(obj => [	obj.brand,	obj.category,	obj.quantity,	obj.revenue]);

    createTsvFile(tsvData, headers, 'SalesReport.tsv');
}
// Search brandCategory
function searchInTwoColumns(columnIndex1, searchValue1, columnIndex2, searchValue2) {
console.log(columnIndex1, searchValue1, columnIndex2, searchValue2);
    table.columns().search('').draw(); // Clear existing search
    table.columns(columnIndex1).search(searchValue1, true, false).draw();
    table.columns(columnIndex2).search(searchValue2, true, false).draw();
}
function findBrandCategory(){
    var $form = $("#orderItem-form");
    var json1 = toJson($form);
    var json = JSON.parse(json1);

    var brand = json.brand.toLowerCase().trim();
    var category = json.category.toLowerCase().trim();
    console.log(brand,category);
    searchInTwoColumns(0, brand, 1, category);
}

function emptyFields() {
    $('input').val('');
    getOrderListByFilter();
}

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(emptyFields);
	$('#upload-data').click(getOrderListByFilter);
	$('#find-brand-data').click(findBrandCategory);
	getOrderListByFilter();
    table = $('#brand-table').DataTable({
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
      order: [[0, "desc"]],
      columns: [
              { searchable: true },  // Column 0 - Searchable
              { searchable: true },  // Column 1 - Searchable
              { searchable: false }, // Column 2 - Not searchable
              { searchable: false },
          ],
    });
}

$(document).ready(init);

