// Global variables for pagination
var table;
//var currentPage = 1;
//var pageSize = 10;
//var totalItems = 0;
//var totalPages = 0;

function getBrandUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/brand";
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
var processCount = 0;

function processData() {
    var file = $('#brandFile')[0].files[0];
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
    var expectedHeaders = ["brand", "category"];

    var extractedHeaders = Object.keys(uploadedHeaders);
//    var filteredArr = extractedHeaders.filter(header=> expectedHeaders.includes(header));
    var filteredArr = extractedHeaders.filter(function(header, index) {
        console.log("header: ",header,"  :  ",index);
      return (expectedHeaders.includes(header.toLowerCase()) && extractedHeaders.indexOf(header) === index);
    });
//    var uniqueHeadersSet = new Set();
//      var filteredArr = extractedHeaders.filter(header => {
//        var headerLowerCase = header.toLowerCase();
//        if (expectedHeaders.includes(headerLowerCase) && !uniqueHeadersSet.has(headerLowerCase)) {
//          uniqueHeadersSet.add(headerLowerCase);
//          return true;
//        }
//        return false;
//      });
    // Compare the headers
    if (filteredArr.length !== expectedHeaders.length) {
        return false;
    }
    // Sort the extracted headers and expected headers in lowercase
//    filteredArr = filteredArr.map(header => header.toLowerCase());
    filteredArr.sort();
//    expectedHeaders = expectedHeaders.map(header => header.toLowerCase());
    expectedHeaders.sort();

    for (var i = 0; i < filteredArr.length; i++) {
        console.log(filteredArr[i],expectedHeaders[i])
    }
//    for (var i = 0; i < expectedHeaders.length; i++) {
//        if (filteredArr[i] !== expectedHeaders[i]) {
//            return false;
//        }
//    }
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
    var url = getBrandUrl();
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
            row.error = response.responseText;
            errorData.push(row);
            uploadRows();
        }
    });
    $("#download-errors").prop('disabled', false);
}

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
    // Display the brand list for the current page

//    var startIndex = (currentPage - 1) * pageSize;
//    var endIndex = startIndex + pageSize;
//    var paginatedData = data.slice(startIndex, endIndex);
    var $tbody = $('#brand-table').find('tbody');
//    $tbody.empty();
    table.clear().draw();
    var dataRows=[];
    for (var i in data) {
        var e = data[i];
        var buttonHtml = ' <button class="btn btn-outline-primary" onclick="displayEditBrand(' + e.id + ')"> Edit </button>';
        var trimmedBrand = e.brand.length > 15 ? e.brand.substring(0, 15) + '...' : e.brand;
        var trimmedCategory = e.category.length > 15 ? e.category.substring(0, 15) + '...' : e.category;
        dataRows.push([e.id, trimmedBrand, trimmedCategory, buttonHtml]);
    }
    table.rows.add(dataRows).draw();
}
//  Pagination
//function displayPaginationInfo() {
//    totalPages = Math.ceil(totalItems / pageSize);
//    var $paginationInfo = $('#pagination-info');
//    $paginationInfo.text('Page ' + currentPage + ' of ' + totalPages);
//}
//
//function goToPage(page) {
//    if (page < 1 || page > Math.ceil(totalItems / pageSize)) {
//        return;
//    }
//    currentPage = page;
//    getBrandList();
//}

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
//    $('#first-page').click(function() {
//        goToPage(1);
//    });
//
//    $('#prev-page').click(function() {
//        goToPage(currentPage - 1);
//    });
//
//    $('#next-page').click(function() {
//        goToPage(currentPage + 1);
//    });
//
//    $('#last-page').click(function() {
//        goToPage(totalPages);
//    });

    // Initial data fetch
    getBrandList();
    table = $('#brand-table').DataTable({
      columnDefs: [
        { targets: [3], orderable: false },
        { targets: [0, 1, 2, 3], className: "text-center" }
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