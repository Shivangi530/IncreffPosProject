
function getOrderItemUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderItem";
}

//BUTTON ACTIONS
function addOrderItem(event){
	//Set the values to update
	var $form = $("#orderItem-form");
	var json = toJson($form);
	var url = getOrderItemUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getOrderItemList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateOrderItem(event){
	$('#edit-orderItem-modal').modal('toggle');
	//Get the ID
	var id = $("#orderItem-edit-form input[name=id]").val();
	var url = getOrderItemUrl() + "/" + id;

	//Set the values to update
	var $form = $("#orderItem-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getOrderItemList();
	   		console.log("json",json);
	   },
	   error: handleAjaxError
	});

	return false;
}


function getOrderItemList(){
	var url = getOrderItemUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderItemList(data.reverse());
	   },
	   error: handleAjaxError
	});
}


function deleteOrderItem(id){
	var url = getOrderItemUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getOrderItemList();
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#orderItemFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getOrderItemUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();  
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayOrderItemList(data){
	var $tbody = $('#orderItem-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="deleteOrderItem(' + e.id + ')">delete</button>'
		buttonHtml += ' <button onclick="displayEditOrderItem(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.orderId + '</td>'
		+ '<td>' + e.productId + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>'  + e.sellingPrice + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditOrderItem(id){
	var url = getOrderItemUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderItem(data);
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#orderItemFile');
	$file.val('');
	$('#orderItemFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#orderItemFile');
	var fileName = $file.val();
	$('#orderItemFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-orderItem-modal').modal('toggle');
}

function displayOrderItem(data){
	$("#orderItem-edit-form input[name=orderId]").val(data.orderId);
	$("#orderItem-edit-form input[name=productId]").val(data.productId);
	$("#orderItem-edit-form input[name=quantity]").val(data.quantity);
	$("#orderItem-edit-form input[name=sellingPrice]").val(data.sellingPrice);
	$("#orderItem-edit-form input[name=id]").val(data.id);
	$('#edit-orderItem-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-orderItem').click(addOrderItem);
	$('#update-orderItem').click(updateOrderItem);
	$('#refresh-data').click(getOrderItemList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#orderItemFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getOrderItemList);

