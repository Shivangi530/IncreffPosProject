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
//    console.log("Hey there",startDate,endDate);
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
    	        salesData=response.reverse();
    	        displayOrderList(response);
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
		var row = '<tr>'
		+ '<td>' + e.date + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>'  + e.revenue + '</td>'
		+ '</tr>';
        dataRows.push([ trimmedBrand, trimmedCategory, e.quantity,e.revenue]);
    }
    table.rows.add(dataRows).draw();
}

function printReport() {
    // Check if data is empty or undefined
    if (!salesData || salesData.length === 0) {
        console.error('No data available to generate the report.');
        return;
    }


    var filteredData = salesData.map(({date,	brand,	category,	quantity,	revenue }) => ({date,	brand,	category,	quantity,	revenue}));

    // Prepare headers and TSV data for the writeFileData function
    var headers = [	'Brand',	'Category',	'Quantity',	'Revenue'];
    var tsvData = filteredData.map(obj => [	obj.brand,	obj.category,	obj.quantity,	obj.revenue]);

    // Call the createTsvFile function with the filtered TSV data and file name
    createTsvFile(tsvData, headers, 'SalesReport.tsv');
}

//function printReport(fileName) {
//        console.log(salesData);
//     // Check if salesData is empty or undefined
//        if (!salesData || salesData.length === 0) {
//            console.error('No salesData available to generate the report.');
//            return;
//        }
//     // Exclude startDate and endDate fields from headers
//       var headers = Object.keys(salesData[0]).filter(function(key) {
//         return key !== 'startDate' && key !== 'endDate';
//       });
//
//       // Exclude startDate and endDate fields from dataArray
//       var dataArray = salesData.map(function(obj) {
//         return headers.map(function(header) {
//           return obj[header];
//         });
//       });
//
//       var tsvData = [headers, ...dataArray];
//       var tsvContent = tsvData.map(function(row) {
//         return row.join('\t');
//       }).join('\n');
//
//       var blob = new Blob([tsvContent], { type: 'text/tab-separated-values' });
//       var url = URL.createObjectURL(blob);
//
//       var link = document.createElement('a');
//       link.href = url;
//       link.download = fileName;
//       link.style.display = 'none';
//
//       document.body.appendChild(link);
//       link.click();
//
//       document.body.removeChild(link);
//       URL.revokeObjectURL(url);
//}

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getOrderListByFilter);
	$('#upload-data').click(getOrderListByFilter);
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
      order: [[0, "desc"]]
    });
}

$(document).ready(init);

