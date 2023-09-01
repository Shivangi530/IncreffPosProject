var table;

function getBrandUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/brand";
}

function getBrandUrlList() {
    var baseUrl = $("meta[name=baseUrl]").attr("content");
    return baseUrl + "/api/brand/list";
}

//BUTTON ACTIONS
function addBrand(event) {
    var $form = $("#brand-form");
    var json = toJson($form);
    var url = getBrandUrl();
    var json1 = JSON.parse(json);
    if(json1.brand == "") {
        warning("Brand cannot be empty");
        return false;
    }
    if(json1.category == "") {
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
    //Get the ID
    var id = $("#brand-edit-form input[name=id]").val();
    var url = getBrandUrl() + "/" + id;
    //Set the values to update
    var $form = $("#brand-edit-form");
    var json = toJson($form);
    var json1 = JSON.parse(json);
    if(json1.brand == "") {
        warning("Brand cannot be empty");
        return false;
    }
    if(json1.category == "") {
        warning("Category cannot be empty");
        return false;
    }
    $.ajax({
        url: url,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            $('#edit-brand-modal').modal('toggle');
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
            data = data.reverse();
            displayBrandList(data);
        },
        error: handleAjaxError
    });
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var fileErrorData = [];
var processCount = 0;

function processData() {
    var file = $('#brandFile')[0].files[0];
    console.log(file);
    var tsv = (file) => file.toLowerCase().endsWith('.tsv');
    if(!tsv) {
        console.log("Invalid file format: Not TSV.");
        warning("Invalid file format: Not TSV.");
        return;
    } else {
        readFileData(file, readFileDataCallback);
    }
}

function readFileDataCallback(results) {
    fileData = results.data;
    if(fileData.length == 0) {
        console.log("File Empty");
        warning("File Empty");
    } else if(fileData.length > 5000) {
        console.log("File size exceeds limit");
        warning("The file size (" + fileData.length + ") exceeds the limit of 5000.");
    } else {
        console.log(fileData[0]);
        // Convert keys to lowercase and trim whitespace
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
        console.log(fileData);
        console.log(fileDataFiltered);
        fileData= fileDataFiltered;
        if(!checkHeaderMatch(fileData[0])) {
            console.log("File headers do not match the expected format");
            warning("File headers do not match the expected format");
            return;
        }
        console.log(fileData);
        uploadRows();
    }
}

function checkHeaderMatch(uploadedHeaders) {
    var expectedHeaders = ["brand", "category"];
    var extractedHeaders = Object.keys(uploadedHeaders);
    extractedHeaders = extractedHeaders.map(header => header.toLowerCase().trim());

    // Compare the headers
    if(extractedHeaders.length !== expectedHeaders.length) {
        return false;
    }
    extractedHeaders.sort();
    for(var i = 0; i < expectedHeaders.length; i++) {
        if(extractedHeaders[i] !== expectedHeaders[i]) {
            return false;
        }
    }
    return true;
}

function uploadRows() {
    //Update progress
    updateUploadDialog();
    var json = JSON.stringify(fileData);
    var url = getBrandUrlList();
    //Make ajax call
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            getBrandList();
            $('#upload-brand-modal').modal('hide');
            success("File uploaded successfully");
        },
        error: function(response) {
            var resp = JSON.parse(response.responseText);
            const inputString = resp.message;
            // Remove the square brackets at the beginning and end of the string
            const cleanedString = inputString.slice(1, -1);

            // Split the string using the comma (,) as the delimiter
            const errorArray = cleanedString.split(', ');

            var i = 0;
            // Loop through the errorArray and format each error as a CSV row
            errorArray.forEach((error) => {
                const index = error.indexOf('=');
                const errorCode = error.slice(0, index);
                const errorMessage = error.slice(index + 1);
                const errorRowIndex = parseInt(errorCode, 10);
                if(!isNaN(errorRowIndex) && errorRowIndex >= 0 && errorRowIndex < fileData.length) {
                    if(!errorData[i]) {
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
}

function downloadErrors() {
    if(errorData.length === 0) {
        $("#download-errors").prop('disabled', true);
        warning("No errors to download");
        return;
    }
    writeFileData(errorData);
}


//UI DISPLAY METHODS

function displayBrandList(data) {
    var $tbody = $('#brand-table').find('tbody');
    table.clear().draw();
    var dataRows = [];
    for(var i in data) {
        var e = data[i];
        var buttonHtml = ' <button class="btn btn-outline-primary" onclick="displayEditBrand(' + e.id + ')"> Edit </button>';
        var trimmedBrand = e.brand.length > 15 ? e.brand.substring(0, 15) + '...' : e.brand;
        var trimmedCategory = e.category.length > 15 ? e.category.substring(0, 15) + '...' : e.category;
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
    fileErrorData = [];
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

    table = $('#brand-table').DataTable({
        columnDefs: [
            { targets: [0, 1, 2, 3], className: "text-center" },
            { targets: 0, visible: false },
            { targets: 3, orderable: false, visible: userRole === 'SUPERVISOR' },
        ],
        searching: true,
        info: true,
        lengthMenu: [
            [5, 10, 20, -1],
            [5, 10, 20, 'All']
        ],
        deferRender: true,
        order: [
            [0, "desc"]
        ],
        columns: [
            { searchable: false },
            { searchable: true },
            { searchable: true },
            { searchable: false }
        ],
    });
}
$(document).ready(init);