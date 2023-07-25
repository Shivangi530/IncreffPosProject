var table;
var inventoryData = [];

function getInventoryUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/inventory";
}

function updateInventory(event) {
    $('#edit-inventory-modal').modal('toggle');
    //Get the ID
    var id = $("#inventory-edit-form input[name=id]").val();
    var url = getInventoryUrl() + "/" + id;

    //Set the values to update
    var $form = $("#inventory-edit-form");
    var json = toJson($form);
    var json1 = JSON.parse(json);
    if ((parseFloat(json1.quantity) - parseInt(json1.quantity)) > 0) {
        warning("Quantity should be of integer type.");
        return;
    }

    $.ajax({
        url: url,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            getInventoryList();
        },
        error: handleAjaxError
    });

    return false;
}


function getInventoryList() {
    var url = getInventoryUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            inventoryData = data.reverse();
            displayInventoryList(inventoryData);
        },
        error: handleAjaxError
    });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData() {
    var file = $('#inventoryFile')[0].files[0];
    //Check if file format is tsv or not
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
    var expectedHeaders = ["barcode", "quantity"];

    var extractedHeaders = Object.keys(uploadedHeaders);
    extractedHeaders = extractedHeaders.filter(item => item !== 'error');
    // Compare the headers
    if (extractedHeaders.length !== expectedHeaders.length) {
        return false;
    }
    // Sort the extracted headers and expected headers in lowercase
    extractedHeaders.sort();
    expectedHeaders = expectedHeaders.map(header => header.toLowerCase());
    expectedHeaders.sort();
    for (var i = 0; i < expectedHeaders.length; i++) {
        if (extractedHeaders[i] !== expectedHeaders[i]) {
            return false;
        }
    }
    return true;
}

function ajaxFxn(data) {
    //Make ajax call

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
    var id = -1;
    var barcode = row.barcode.toLowerCase();
    var quantity = row.quantity;
    for (var i in inventoryData) {
        if (inventoryData[i].barcode === barcode) {
            id = inventoryData[i].id;
            quantity = parseInt(quantity) + parseInt(inventoryData[i].quantity);
            break;
        }
    }
    console.log("True id:", id);
    if (id === -1) {
        row.error = "Barcode: " + barcode + " does not exist";
        errorData.push(row);
        uploadRows();
    } else {
        console.log("True id:", id);
        var jsonData = {
            id: id,
            quantity: quantity
        };
        var json = JSON.stringify(jsonData);
        var url = getInventoryUrl() + "/" + id;
        $.ajax({
            url: url,
            type: 'PUT',
            data: json,
            headers: {
                'Content-Type': 'application/json'
            },
            success: function(response) {
                uploadRows();
                getInventoryList();
            }
        });
    }
    console.log("id====", id);

    $("#download-errors").prop('disabled', false);
}

function downloadErrors() {
    writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayInventoryList(data) {
    table.clear().draw();
    var dataRows=[];
    for (var i in data) {
        var e = data[i];
        //var buttonHtml = '<button onclick="deleteInventory(' + e.id + ')">delete</button>'
        var buttonHtml = ' <button class="btn btn-outline-primary" onclick="displayEditInventory(' + e.id + ')">Edit</button>'
        var row = '<tr>' +
            '<td>' + e.barcode + '</td>' +
            '<td>' + e.quantity + '</td>' +
            '<td>' + buttonHtml + '</td>' +
            '</tr>';
        dataRows.push([e.barcode, e.quantity, buttonHtml]);
    }
    table.rows.add(dataRows).draw();
}

function displayEditInventory(id) {
    var url = getInventoryUrl() + "/" + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayInventory(data);
        },
        error: handleAjaxError
    });
}

function resetUploadDialog() {
    //Reset file name
    var $file = $('#inventoryFile');
    $file.val('');
    $('#inventoryFileName').html("Choose File");
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
    var $file = $('#inventoryFile');
    var fileName = $file.val();
    $('#inventoryFileName').html(fileName);
}

function displayUploadData() {
    $("#download-errors").prop('disabled', true);
    resetUploadDialog();
    $('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data) {
    $("#inventory-edit-form input[name=quantity]").val(data.quantity);
    $("#inventory-edit-form input[name=id]").val(data.id);
    $('#edit-inventory-modal').modal('toggle');
}


//INITIALIZATION CODE
function init() {
    //$('#add-inventory').click(addInventory);
    $('#update-inventory').click(updateInventory);
    $('#refresh-data').click(getInventoryList);
    $('#upload-data').click(displayUploadData);
    $('#process-data').click(processData);
    $('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName);
    getInventoryList();
   table = $('#inventory-table').DataTable({
      columnDefs: [
        { targets: [2], orderable: false },
        { targets: [0, 1, 2], className: "text-center" }
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
$(document).ready(getInventoryList);