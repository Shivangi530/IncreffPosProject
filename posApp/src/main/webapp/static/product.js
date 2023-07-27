var table;

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
    if(json1.barcode === ""){
        warning("Barcode cannot be empty");
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
    if(json1.name === ""){
        warning("Name cannot be empty");
        return false;
    }
    if(json1.mrp === ""){
        warning("Mrp cannot be empty");
        return false;
    }
    if(parseFloat(json1.mrp)>1000000000){
        warning("Mrp entered is too large");
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
            success("Success");
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
    var json1 = JSON.parse(json);
    if (json1.brand == "") {
        warning("Brand cannot be empty");
        return false;
    }
    if(json1.barcode === ""){
        warning("Barcode cannot be empty");
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
    if(json1.name === ""){
        warning("Name cannot be empty");
        return false;
    }
    if(json1.mrp === ""){
        warning("Mrp cannot be empty");
        return false;
    }
    if(parseFloat(json1.mrp)>1000000000){
        warning("Mrp entered is too large");
        return false;
    }
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
    var expectedHeaders = [ "barcode", "brand", "category", "name", "mrp"];
    var extractedHeaders = Object.keys(uploadedHeaders).map(header => header.toLowerCase());
    var filteredArr = extractedHeaders.filter(item => item !== 'error');
//    var filteredArr = extractedHeaders.filter(item => item.trim() !== '');
    console.log(extractedHeaders);
    console.log(expectedHeaders);
    // Compare the headers
    if (filteredArr.length !== expectedHeaders.length) {
        return false;
    }
    // Sort the extracted headers and expected headers in lowercase
    filteredArr = filteredArr.map(header => header.toLowerCase());
    filteredArr.sort();
    expectedHeaders = expectedHeaders.map(header => header.toLowerCase());
    expectedHeaders.sort();
    for (var i = 0; i < expectedHeaders.length; i++) {
        if (filteredArr[i] !== expectedHeaders[i]) {
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
            refresh();
        },
        error: function(response) {
            row.error = response.responseText
            errorData.push(row);
            uploadRows();
        }
    });
    $("#download-errors").prop('disabled', false);
}

function downloadErrors() {
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
        var row = '<tr>' +
            '<td>' + e.id + '</td>' +
            '<td>' + e.barcode + '</td>' +
            '<td>' + trimmedBrand + '</td>' +
            '<td>' + trimmedCategory + '</td>' +
            '<td>' + trimmedName + '</td>' +
            '<td>' + e.mrp + '</td>' +
            '<td>' + buttonHtml + '</td>' +
            '</tr>';
        dataRows.push([e.id,e.barcode, trimmedBrand, trimmedCategory,trimmedName,e.mrp, buttonHtml]);
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
        { targets: [6], orderable: false },
        { targets: [0, 1, 2, 3,4,5,6], className: "text-center" }
      ],
      searching: false,
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
//$(document).ready(getProductList);