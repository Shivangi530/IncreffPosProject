var table;

function getBrandUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/brand";
}

function getBrandUrlList(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/brands/list";
}

//BUTTON ACTIONS
function addBrand(event) {
    var $form = $("#brand-form");
    var json = toJson($form);
    var url = getBrandUrl();
    console.log(url);
    var json1 = JSON.parse(json);
    if (json1.brand == "") {
        warning("Brand cannot be empty");
        return false;
    }
    if (json1.category == "") {
        warning("Category cannot be empty");
        return false;
    }
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            success("Success");
            getBrandList();
            resetFormFields($form);
        },
        error: handleAjaxError
    });
    return false;
}

function updateBrand(event) {
    $('#edit-brand-modal').modal('toggle');
    //Get the ID
    var id = $("#brand-edit-form input[name=id]").val();
    var url = getBrandUrl() + "/" + id;
    //Set the values to update
    var $form = $("#brand-edit-form");
    // Check if any input field is empty
//    var brandValue= $("#brand-edit-form input[name=brand]").val();
//    var categoryValue=    $("#brand-edit-form input[name=category]").val();
//    var isEmpty = brandValue === '' || categoryValue === '' ;
//    var isEmpty = $('input').val() === "";
    // Disable or enable the button based on the isEmpty variable
//    $('#update-brand').prop('disabled', isEmpty);
    var json = toJson($form);
    $.ajax({
        url: url,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            success("Item Updated");
            getBrandList();
        },
        error: handleAjaxError
    });
    return false;
}

function getBrandList() {
    var url = getBrandUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            data= data.reverse();
//            totalItems = data.length;
            displayBrandList(data);
//            displayPaginationInfo();
        },
        error: handleAjaxError
    });
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var fileErrorData=[];
var processCount = 0;

function processData(){
	var file = $('#brandFile')[0].files[0];
	checkHeader(file,["brand","category"],readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows() {
    //Update progress
    updateUploadDialog();
    var row = fileData;
    var json = JSON.stringify(row);
    var url = getBrandUrlList();
    console.log(json);
    //Make ajax call
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            console.log(response);
            getBrandList();
            $('#upload-brand-modal').modal('hide');
            success("File uploaded successfully");
        },
        error: function(response) {
            console.log(response);
            var resp = JSON.parse(response.responseText);
            const inputString = resp.message;
            // Remove the square brackets at the beginning and end of the string
            const cleanedString = inputString.slice(1, -1);

            // Split the string using the comma (,) as the delimiter
            const errorArray = cleanedString.split(', ');

            var i=0;
            // Loop through the errorArray and format each error as a CSV row
            errorArray.forEach((error) => {
              const index = error.indexOf('=');
              const errorCode = error.slice(0, index);
              const errorMessage = error.slice(index + 1);
              const errorRowIndex = parseInt(errorCode, 10);
                if (!isNaN(errorRowIndex) && errorRowIndex >= 0 && errorRowIndex < fileData.length) {
                  if (!errorData[i]) {
                    errorData[i] = Object.assign({}, fileData[errorRowIndex], { error: errorMessage });
                    i++;
                  }
                }
            });

//            for (var i = 0; i < fileData.length; i++) {
//              var row = fileData[i];
//              var error = (i < errorData.length) ? errorData[i][0] : '';
//              var combinedRow = Object.assign({}, row, { error: error });
//              fileErrorData.push(combinedRow);
//            }
            updateUploadDialog();
            $("#download-errors").prop('disabled', false);
            danger("Invalid file: File has errors");
        }
    });
}

//function checkHeaderMatch(uploadedHeaders) {
//    // Extract the headers from the object
//    var expectedHeaders = ["brand", "category"];
//
//    var extractedHeaders = Object.keys(uploadedHeaders);
////    var filteredArr = extractedHeaders.filter(header=> expectedHeaders.includes(header));
//    var filteredArr = extractedHeaders.filter(function(header, index) {
//        console.log("header: ",header,"  :  ",index);
//      return (expectedHeaders.includes(header.toLowerCase()) && extractedHeaders.indexOf(header) === index);
//    });
//    if (filteredArr.length !== expectedHeaders.length) {
//        return false;
//    }
//    filteredArr.sort();
//    expectedHeaders.sort();
//
//    for (var i = 0; i < filteredArr.length; i++) {
//        console.log(filteredArr[i],expectedHeaders[i])
//    }
//    return true;
//}

function downloadErrors() {
    console.log(errorData);
    if (errorData.length === 0) {
        $("#download-errors").prop('disabled', true);
        warning("No errors to download");
        return;
    }
    writeFileData(errorData);
}


//UI DISPLAY METHODS

function displayBrandList(data) {
    console.log("userRole= ",userRole);
    var $tbody = $('#brand-table').find('tbody');
    table.clear().draw();
    var dataRows=[];
    for (var i in data) {
        var e = data[i];
        var buttonHtml = ' <button class="btn btn-outline-primary" onclick="displayEditBrand(' + e.id + ')"> Edit </button>';
        var trimmedBrand = e.brand.length > 15 ? e.brand.substring(0, 15) + '...' : e.brand;
        var trimmedCategory = e.category.length > 15 ? e.category.substring(0, 15) + '...' : e.category;

//        if (userRole === "SUPERVISOR") {
        dataRows.push([e.id, trimmedBrand, trimmedCategory, buttonHtml]);

    }
    table.rows.add(dataRows).draw();
}

function displayEditBrand(id) {
    var url = getBrandUrl() + "/" + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayBrand(data);
        },
        error: handleAjaxError
    });
}

function resetUploadDialog() {
    //Reset file name
    var $file = $('#brandFile');
    $file.val('');
    $('#brandFileName').html("Choose File");
    //Reset various counts
    processCount = 0;
    fileData = [];
    errorData = [];
    fileErrorData =[];
    //Update counts
    updateUploadDialog();
}

function updateUploadDialog() {
    $('#rowCount').html("" + fileData.length);
//    $('#processCount').html("" + processCount);
    $('#errorCount').html("" + errorData.length);
}

function updateFileName() {
    processCount = 0;
    fileData = [];
    errorData = [];
    updateUploadDialog();
    $("#download-errors").prop('disabled', true);
    var $file = $('#brandFile');
    var fileName = $file.val();
    $('#brandFileName').html(fileName);
}

function displayUploadData() {
    $("#download-errors").prop('disabled', true);
    resetUploadDialog();
    $('#upload-brand-modal').modal('toggle');
}

function displayBrand(data) {
    $("#brand-edit-form input[name=brand]").val(data.brand);
    $("#brand-edit-form input[name=category]").val(data.category);
    $("#brand-edit-form input[name=id]").val(data.id);
    $('#edit-brand-modal').modal('toggle');
}

function refresh() {
    getBrandList();
    $('input').val('');
}

//INITIALIZATION CODE
function init() {
    $('#add-brand').click(addBrand);
    $('#update-brand').click(updateBrand);
    $('#refresh-data').click(refresh);
    $('#upload-data').click(displayUploadData);
    $('#process-data').click(processData);
    $('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);

    // Initial data fetch
    getBrandList();
//    table = $('#brand-table').DataTable({
//            columnDefs: [
//                { targets: [0, 1, 2], className: 'text-center', width: '100px' }, // Optional: Set a fixed width for columns 1 and 2
//                { targets: 3, orderable: false, visible: userRole === 'SUPERVISOR', width: '100px' } // Optional: Set a fixed width for column 3
//            ],
//            scrollX: true,
//            deferRender: true,
//            scrollCollapse: true,
//            scroller: true,
//            searching: true,
//            info: true,
//            lengthMenu: [
//                [5, 10, 20, -1],
//                [5, 10, 20, 'All']
//            ],
//            order: [[0, 'desc']],
//        });
    table = $('#brand-table').DataTable({
//      scrollX: true,
      columnDefs: [
        { targets: [0, 1, 2, 3], className: "text-center" },
        { targets: 3, orderable: false, visible: userRole === 'SUPERVISOR'},
      ],
      searching: false,
      info: true,
      lengthMenu: [
        [5, 10, 20, -1],
        [5, 10, 20, 'All']
      ],
      deferRender: true,
      order: [[0, "desc"]],
    });
}
$(document).ready(init);