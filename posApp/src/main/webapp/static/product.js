// Global variables for pagination
var currentPage = 1;
var pageSize = 10;
var totalItems = 0;

function getProductUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/product";
}

//BUTTON ACTIONS
function addProduct(event) {
    //Set the values to update
    var $form = $("#product-form");
    var json = toJson($form);
    var url = getProductUrl();
    var json1 = JSON.parse(json);
    if (json1.brand == "") {
        warning("Brand cannot be empty");
        return false;
    }
    if (json1.category == "") {
        warning("Category cannot be empty");
        return false;
    }
    console.log(json);

    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            success("Item added successfully");
            getProductList();
            resetFormFields($form);
        },
        error: handleAjaxError
    });

    return false;
}

function updateProduct(event) {
    $('#edit-product-modal').modal('toggle');
    //Get the ID
    var id = $("#product-edit-form input[name=id]").val();
    var url = getProductUrl() + "/" + id;

    //Set the values to update
    var $form = $("#product-edit-form");
    var json = toJson($form);
    console.log(json);
    $.ajax({
        url: url,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            getProductList();
        },
        error: handleAjaxError
    });
    return false;
}


function getProductList() {
    var url = getProductUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayProductList(data);
        },
        error: handleAjaxError
    });
}

function deleteProduct(id) {
    var url = getProductUrl() + "/" + id;

    $.ajax({
        url: url,
        type: 'DELETE',
        success: function(data) {
            getProductList();
        },
        error: handleAjaxError
    });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;

function processData() {
    var file = $('#productFile')[0].files[0];
    var tsv = (file) => file.toLowerCase().endsWith('.tsv');
    if (!tsv) {
        console.log("Invalid file format: Not TSV.");
        warning("Invalid file format: Not TSV.");
        return;
    } else {
        readFileData(file, readFileDataCallback);
    }
}

function readFileDataCallback(results) {
    fileData = results.data;
    if (fileData.length == 0) {
        console.log("File Empty");
        warning("File Empty");
    } else if (fileData.length > 5000) {
        console.log("File size exceeds limit");
        warning("The file size (" + fileData.length + ") exceeds the limit of 5000.");
    } else {
        if (!checkHeaderMatch(fileData[0])) {
            console.log("File headers do not match the expected format");
            warning("File headers do not match the expected format");
            return;
        }
        uploadRows();
    }
}
function checkHeaderMatch(uploadedHeaders) {
  // Extract the headers from the object
  var expectedHeaders = ["id","barcode", "brand", "category", "name", "mrp"];
  var extractedHeaders = Object.keys(uploadedHeaders).map(header => header.toLowerCase());;
  // Compare the headers
  if (extractedHeaders.length !== expectedHeaders.length) {
    return false;
  }
  // Sort the extracted headers and expected headers in lowercase
    extractedHeaders.sort();
    expectedHeaders = expectedHeaders.map(header => header.toLowerCase());
    expectedHeaders.sort();
  for (var i = 0; i < expectedHeaders.length; i++) {
    if (extractedHeaders[i]!== expectedHeaders[i]) {
      return false;
    }
  }
  return true;
}

function uploadRows() {
    //Update progress
    updateUploadDialog();
    //If everything processed then return
    if (processCount == fileData.length) {
        return;
    }

    //Process next row
    var row = fileData[processCount];
    processCount++;

    var json = JSON.stringify(row);
    var url = getProductUrl();

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
        error: function(response) {
            row.error = response.responseText
            errorData.push(row);
            uploadRows();
        }
    });

}

function downloadErrors() {
    writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayProductList(data) {
    var $tbody = $('#product-table').find('tbody');
    $tbody.empty();
    for (var i in data) {
        var e = data[i];
        var buttonHtml = ' <button onclick="displayEditProduct(' + e.id + ')">edit</button>'
        var row = '<tr>' +
            '<td>' + e.id + '</td>' +
            '<td>' + e.barcode + '</td>' +
            '<td>' + e.brand + '</td>' +
            '<td>' + e.category + '</td>' +
            '<td>' + e.name + '</td>' +
            '<td>' + e.mrp + '</td>' +
            '<td>' + buttonHtml + '</td>' +
            '</tr>';
        $tbody.append(row);
    }
}

function displayPaginationInfo() {
    var totalPages = Math.ceil(totalItems / pageSize);
    var $paginationInfo = $('#pagination-info');
    $paginationInfo.text('Page ' + currentPage + ' of ' + totalPages);
}

function goToPage(page) {
    if (page < 1 || page > Math.ceil(totalItems / pageSize)) {
        return;
    }
    currentPage = page;
    getBrandList();
}

function displayEditProduct(id) {
    var url = getProductUrl() + "/" + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayProduct(data);
        },
        error: handleAjaxError
    });
}

function resetUploadDialog() {
    //Reset file name
    var $file = $('#productFile');
    $file.val('');
    $('#productFileName').html("Choose File");
    //Reset various counts
    processCount = 0;
    fileData = [];
    errorData = [];
    //Update counts
    updateUploadDialog();
}

function updateUploadDialog() {
    $('#rowCount').html("" + fileData.length);
    $('#processCount').html("" + processCount);
    $('#errorCount').html("" + errorData.length);
}

function updateFileName() {
    var $file = $('#productFile');
    var fileName = $file.val();
    $('#productFileName').html(fileName);
}

function displayUploadData() {
    resetUploadDialog();
    $('#upload-product-modal').modal('toggle');
}

function displayProduct(data) {
    $("#product-edit-form input[name=barcode]").val(data.barcode);
    $("#product-edit-form input[name=brand]").val(data.brand);
    $("#product-edit-form input[name=category]").val(data.category);
    $("#product-edit-form input[name=name]").val(data.name);
    $("#product-edit-form input[name=mrp]").val(data.mrp);
    $("#product-edit-form input[name=id]").val(data.id);
    $('#edit-product-modal').modal('toggle');
}


//INITIALIZATION CODE
function init() {
    $('#add-product').click(addProduct);
    $('#update-product').click(updateProduct);
    $('#refresh-data').click(getProductList);
    $('#upload-data').click(displayUploadData);
    $('#process-data').click(processData);
    $('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getProductList);