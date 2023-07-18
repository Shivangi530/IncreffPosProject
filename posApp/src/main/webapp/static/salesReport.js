var data = [];
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/salesReport";
}
function getOrderUrl2(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/salesReport/relevent";
}
function getOrderList(){
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(response) {
	        data=response;
	        console.log(data);
	   		displayOrderList(response);
	   },
	   error: handleAjaxError
	});
}
function getReleventOrderList(){
	var url = getOrderUrl2();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(response) {
	        data=response;
	   		displayOrderList(response);
	   },
	   error: handleAjaxError
	});
}

function getOrderListByFilter(){
	var url = getOrderUrl();
	var $form = $("#orderItem-form");
    var json1 = toJson($form);
    var json = JSON.parse(json1);
    console.log("json:",json);
    console.log("json.startDate:",json.startDate);
    if(json1.startDate===undefined){
      var currentDate = new Date();
      var oneMonthAgo = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, currentDate.getDate());
      var formattedDate = oneMonthAgo.toISOString().split('T')[0];
        document.getElementById("inputStartDate").value=formattedDate;
        json.startDate=formattedDate;
        console.log("formattedDate:",formattedDate);
        console.log("json.startDate:",json.startDate);
    }
    if(json1.endDate===undefined){
          var currentDate = new Date();
          var formattedDate = currentDate.toISOString().split('T')[0];
            document.getElementById("inputEndDate").value=formattedDate;
            json.endDate=formattedDate;
            console.log("formattedDate:",formattedDate);
            console.log("json.startDate:",json.startDate);
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
    	        console.log("Success........");
//    	        console.log("response=======",response);
    	        displayOrderList(response);
    	        console.log("Completed!!!!!!");
    	   },
    	   error: handleAjaxError
    	});
}

//UI DISPLAY METHODS

function displayOrderList(data){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	console.log("displayBrandList.............");
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.date + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>'  + e.revenue + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

//function printReport(fileName) {
//     // Check if data is empty or undefined
//        if (!data || data.length === 0) {
//            console.error('No data available to generate the report.');
//            return;
//        }
//     // Exclude startDate and endDate fields from headers
//       var headers = Object.keys(data[0]).filter(function(key) {
//         return key !== 'startDate' && key !== 'endDate';
//       });
//
//       // Exclude startDate and endDate fields from dataArray
//       var dataArray = data.map(function(obj) {
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
}

$(document).ready(init);
$(document).ready(getOrderListByFilter);

