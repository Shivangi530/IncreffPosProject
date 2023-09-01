var table;

function getProductUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/product";
}
function getProductListUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/product/list";
}

//BUTTON ACTIONS
function addProduct(event) {
    //Set the values to update
    var $form = $("#product-form");
    var json = toJson($form);
    var url = getProductUrl();
    var json1 = JSON.parse(json);
    if(json1.barcode === ""){
        warning("Barcode cannot be empty");
        return false;
    }
    if(json1.name === ""){
        warning("Name cannot be empty");
        return false;
    }
    if(json1.mrp === ""){
        warning("Mrp cannot be empty");
        return false;
    }
    if(parseFloat(json1.mrp)>10000000){
        warning("Mrp entered is too large");
        return false;
    }
    if(parseFloat(json1.mrp)<0.01){
        warning("Mrp must be more than or equal to 0.01");
        return false;
    }
    if(json1.brand === ""){
        warning("Brand cannot be empty");
        return false;
    }
    if(json1.category === ""){
        warning("Category cannot be empty");
        return false;
    }

    json1.mrp=parseFloat(json1.mrp).toFixed(2);
    console.log(json);

    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify( json1),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            success("Success");
            getProductList();
            resetFormFields($form);
        },
        error: handleAjaxError
    });
    return false;
}

function updateProduct(event) {
    //Get the ID
    var id = $("#product-edit-form input[name=id]").val();
    var url = getProductUrl() + "/" + id;

    //Set the values to update
    var $form = $("#product-edit-form");
    var json = toJson($form);
    var json1 = JSON.parse(json);
    if(json1.name === ""){
        warning("Name cannot be empty");
        return false;
    }
    if(json1.mrp === ""){
        warning("Mrp cannot be empty");
        return false;
    }
    if(parseFloat(json1.mrp)>10000000){
        warning("Mrp entered is too large");
        return false;
    }
    if(parseFloat(json1.mrp)<0.01){
            warning("Mrp must be more than or equal to 0.01");
            return false;
    }
    json1.mrp=parseFloat(json1.mrp).toFixed(2);
    $.ajax({
        url: url,
        type: 'PUT',
        data: JSON.stringify(json1),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            $('#edit-product-modal').modal('toggle');
            success("Item Updated");
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
var fileErrorData=[];
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
        const firstRecord = fileData[0];
        const updatedKeys = Object.keys(firstRecord).map(key => key.toLowerCase().trim());

        const fileDataFiltered = fileData.map(obj => {
          const lowercaseHeaders = {};
          Object.keys(obj).forEach(key => {
            const lowercaseKey = key.toLowerCase().trim();
            if (lowercaseKey !== "") {  // Skip empty headers
                lowercaseHeaders[lowercaseKey] = obj[key];
            }
          });
          return lowercaseHeaders;
        });
        fileData= fileDataFiltered;
        if (!checkHeaderMatch(fileData[0])) {
            console.log("File headers do not match the expected format");
            warning("File headers do not match the expected format");
            return;
        }
        uploadRows();
    }
}

function checkHeaderMatch(uploadedHeaders) {
    var expectedHeaders = ["barcode", "brand", "category", "mrp", "name"];
    var extractedHeaders = Object.keys(uploadedHeaders);
    extractedHeaders = extractedHeaders.map(header => header.toLowerCase().trim());

    if (extractedHeaders.length !== expectedHeaders.length) {
        return false;
    }
    extractedHeaders.sort();
    for (var i = 0; i < expectedHeaders.length; i++) {
        if (extractedHeaders[i] !== expectedHeaders[i]) {
            return false;
        }
    }
    return true;
}

function uploadRows() {
    //Update progress
    updateUploadDialog();
    var row= fileData;
    var json = JSON.stringify(row);
    var url = getProductListUrl();
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
            $('#upload-product-modal').modal('hide');
            refresh();
            success("File uploaded successfully");
        },
        error: function(response) {
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
            updateUploadDialog();
            $("#download-errors").prop('disabled', false);
            danger("Invalid file: File has errors");

        }
    });
//    $("#download-errors").prop('disabled', false);
}

function downloadErrors() {
    if (errorData.length === 0) {
        $("#download-errors").prop('disabled', true);
        warning("No errors to download");
        return;
    }
    writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayProductList(data) {
    table.clear().draw();
    var dataRows=[];
    for (var i in data) {
        var e = data[i];
        var trimmedBrand = e.brand.length > 15 ? e.brand.substring(0, 15) + '...' : e.brand;
        var trimmedCategory = e.category.length > 15 ? e.category.substring(0, 15) + '...' : e.category;
        var trimmedName = e.name.length > 30 ? e.name.substring(0, 30) + '...' : e.name;
        var buttonHtml = ' <button class="btn btn-outline-primary" onclick="displayEditProduct(' + e.id + ')">Edit</button>'
        dataRows.push([e.id,e.barcode, trimmedBrand, trimmedCategory,trimmedName,parseFloat(e.mrp).toFixed(2), buttonHtml]);
    }
    table.rows.add(dataRows).draw();
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
    fileErrorData=[];
    //Update counts
    updateUploadDialog();
}

function updateUploadDialog() {
    $('#rowCount').html("" + fileData.length);
    $('#processCount').html("" + processCount);
    $('#errorCount').html("" + errorData.length);
}

function updateFileName() {
    processCount = 0;
    fileData = [];
    errorData = [];
    updateUploadDialog();
    $("#download-errors").prop('disabled', true);
    var $file = $('#productFile');
    var fileName = $file.val();
    $('#productFileName').html(fileName);
}

function displayUploadData() {
    $("#download-errors").prop('disabled', true);
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

function refresh() {
    getProductList();
    resetFormFields($('#product-form'));
}

//INITIALIZATION CODE
function init() {
    $('#add-product').click(addProduct);
    $('#update-product').click(updateProduct);
    $('#refresh-data').click(refresh);
    $('#upload-data').click(displayUploadData);
    $('#process-data').click(processData);
    $('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName);
    getProductList();
    table = $('#product-table').DataTable({
      columnDefs: [
        { targets: [0, 1, 2, 3,4,5,6], className: "text-center" },
        { targets: 0, visible: false},
        {
          targets: 6,
          orderable: false,
          visible: userRole === 'SUPERVISOR'
        }
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
                       { searchable: false },
                       { searchable: true },
                       { searchable: true },
                       { searchable: true },
                       { searchable: true },
                       { searchable: false }
                   ],
    });
}

$(document).ready(init);