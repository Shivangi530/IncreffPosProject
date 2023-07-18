// Global variables for pagination
var currentPage = 1;
var pageSize = 10;
var totalItems = 0;

function getBrandUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/brand";
}
//BUTTON ACTIONS
function addBrand(event) {
    //Set the values to update
    var $form = $("#brand-form");
    var json = toJson($form);
    var url = getBrandUrl();
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
            success("Item added successfully");
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
            totalItems = data.length;
            displayBrandList(data);
            displayPaginationInfo();
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
        if (!checkHeaderMatch(fileData[0], expectedHeaders)) {
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
            row.error = response.responseText
            errorData.push(row);
            uploadRows();
        }
    });
}
//function checkHeaderMatch(uploadedHeaders, expectedHeaders) {
//
//    // Compare the headers
//    if (uploadedHeaders.length !== expectedHeaders.length) {
//      console.log('Header length mismatch');
//      return false;
//    }
//
//    for (var i = 0; i < expectedHeaders.length; i++) {
//      if (uploadedHeaders[i] !== expectedHeaders[i]) {
//        console.log('Header mismatch at index ' + i);
//        return false;
//      }
//    }
//    console.log('Headers match');
//    return true;
//}
function downloadErrors() {
    writeFileData(errorData);
}
//UI DISPLAY METHODS
function displayBrandList(data) {
    // Display the brand list for the current page
    var startIndex = (currentPage - 1) * pageSize;
    var endIndex = startIndex + pageSize;
    var paginatedData = data.slice(startIndex, endIndex);
    var $tbody = $('#brand-table').find('tbody');
    $tbody.empty();
    for (var i in paginatedData) {
        var e = paginatedData[i];
        var buttonHtml = ' <button class="btn btn-primary" onclick="displayEditBrand(' + e.id + ')"> Edit </button>';
        var trimmedBrand = e.brand.length > 15 ? e.brand.substring(0, 15) + '...' : e.brand;
        var row = '<tr>' +
            '<td>' + e.id + '</td>' +
            '<td>' + trimmedBrand + '</td>' +
            '<td>' + e.category + '</td>' +
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
    var $file = $('#brandFile');
    var fileName = $file.val();
    $('#brandFileName').html(fileName);
}

function displayUploadData() {
    resetUploadDialog();
    $('#upload-brand-modal').modal('toggle');
}

function displayBrand(data) {
    $("#brand-edit-form input[name=brand]").val(data.brand);
    $("#brand-edit-form input[name=category]").val(data.category);
    $("#brand-edit-form input[name=id]").val(data.id);
    $('#edit-brand-modal').modal('toggle');
}

function togglePlaceholder() {
    var inputElement = document.getElementsByTagName("input");
    inputElement.value = "";
}
//INITIALIZATION CODE
function init() {
    $('#add-brand').click(addBrand);
    $('#update-brand').click(updateBrand);
    $('#refresh-data').click(getBrandList);
    $('#refresh-data').click(togglePlaceholder);
    $('#upload-data').click(displayUploadData);
    $('#process-data').click(processData);
    $('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
    $('#prev-page').click(function() {
        goToPage(currentPage - 1);
    });
    $('#next-page').click(function() {
        goToPage(currentPage + 1);
    });
    // Initial data fetch
    getBrandList();
}
$(document).ready(init);
//$(document).ready(getBrandList);

//function getBrandUrl() {
//    var baseUrl = $("meta[name=baseUrl]").attr("content")
//    return baseUrl + "/api/brand";
//}
////function validateForm() {
////    const form = document.getElementById('brand-form');
////    const errorMessagesDiv = document.getElementById('errorMessages');
////    console.log("inside validateForm");
////    const addButton = document.getElementById("add-button");
////    form.addEventListener(addButton, (event) => {
////      console.log("add-form");
////      event.preventDefault();
////      errorMessagesDiv.innerHTML = '';
////      const requiredFields = form.querySelectorAll('[required]');
////      requiredFields.forEach((field) => {
////        if (field.value.trim() === '') {
////          const fieldName = field.getAttribute('name');
////          const errorMessage = document.createElement('p');
////          errorMessage.textContent = `${fieldName} is required.`;
////          errorMessagesDiv.appendChild(errorMessage);
////        }
////      });
////    });
////}
//function validateForm() {
//  var flag=true;
//  const form = document.getElementById('brand-form');
//  const errorMessagesDiv = document.getElementById('errorMessages');
//  const addButton = document.getElementById("add-brand");
//  console.log("validateForm");
//
//  addButton.addEventListener('click', (event) => {
//    console.log("validateForm->addButton");
//    event.preventDefault();
//    errorMessagesDiv.innerHTML = '';
//    const requiredFields = form.querySelectorAll('[required]');
//    requiredFields.forEach((field) => {
//      if (field.value.trim() === '') {
//        flag=false;
//        console.log("flag is false ",flag);
//        console.log("validateForm->addButton-> if");
//        const fieldName = field.getAttribute('name');
//        const errorMessage = document.createElement('p');
//        errorMessage.textContent = `${fieldName} is required.`;
//        errorMessagesDiv.appendChild(errorMessage);
//        return flag;
//      }
//    });
//  });
//  console.log("The value of the flag is:::::::::::::::::",flag);
//  return flag;
//}
//
////BUTTON ACTIONS
//function addBrand(event) {
////    console.log("validateForm= ",validateForm())
//    if(!validateForm())     return;
//    //Set the values to update
//    var $form = $("#brand-form");
//    var json = toJson($form);
//    var url = getBrandUrl();
//    var jsonObj= JSON.parse(json);
////    console.log("json= ",jsonObj);
////    console.log("json.brand and json.category",jsonObj.brand,jsonObj.category);
//
////    console.log("Done");
//
//    if(jsonObj.brand==""){
//    function f1() {
//        danger("brand cant be empty");
////        console.log("chal gya main");
//    }
//    f1();
//    return;
//    }
////    if(jsonObj.category==""){
////    danger("category cant be empty");
////    return;
////    }
//    $.ajax({
//        url: url,
//        type: 'POST',
//        data: json,
//        headers: {
//            'Content-Type': 'application/json'
//        },
//        success: function(response) {
//            getBrandList();
//            var element = document.getElementById("brand-form");
//            element.reset();
//        },
//        error: handleAjaxError
//    });
//
//    this
//    return false;
//}
//
//function updateBrand(event) {
//    $('#edit-brand-modal').modal('toggle');
//    //Get the ID
//    var id = $("#brand-edit-form input[name=id]").val();
//    var url = getBrandUrl() + "/" + id;
//
//    //Set the values to update
//    var $form = $("#brand-edit-form");
//    var json = toJson($form);
//
//    $.ajax({
//        url: url,
//        type: 'PUT',
//        data: json,
//        headers: {
//            'Content-Type': 'application/json'
//        },
//        success: function(response) {
//            getBrandList();
//        },
//        error: handleAjaxError
//    });
//
//    return false;
//}
//
//function getBrandList() {
//    var url = getBrandUrl();
//    $.ajax({
//        url: url,
//        type: 'GET',
//        success: function(data) {
//            displayBrandList(data);
//
//        },
//        error: handleAjaxError
//    });
//}
//
//// FILE UPLOAD METHODS
//var fileData = [];
//var errorData = [];
//var processCount = 0;
//
//
//function processData() {
//    var file = $('#brandFile')[0].files[0];
//    readFileData(file, readFileDataCallback);
//}
//
//function readFileDataCallback(results) {
//    fileData = results.data;
//    uploadRows();
//}
//
//function uploadRows() {
//    //Update progress
//    updateUploadDialog();
//    //If everything processed then return
//    if (processCount == fileData.length) {
//        return;
//    }
//
//    //Process next row
//    var row = fileData[processCount];
//    processCount++;
//
//    var json = JSON.stringify(row);
//    var url = getBrandUrl();
//
//    //Make ajax call
//    $.ajax({
//        url: url,
//        type: 'POST',
//        data: json,
//        headers: {
//            'Content-Type': 'application/json'
//        },
//        success: function(response) {
//            uploadRows();
//        },
//        error: function(response) {
//            row.error = response.responseText
//            errorData.push(row);
//            uploadRows();
//        }
//    });
//
//}
//
//function downloadErrors() {
//    writeFileData(errorData);
//}
//
////UI DISPLAY METHODS
//
//function displayBrandList(data) {
//    var itemsPerPage = 10; // Number of items to display per page
//    var currentPage = 1; // Current page
//    displayPage(data);
//    function displayPage(data) {
//        var $tbody = $('#brand-table').find('tbody');
//        $tbody.empty();
//        var totalPages = Math.ceil(data.length / itemsPerPage); // Calculate the total number of pages
//        var startIndex = (currentPage - 1) * itemsPerPage; // Calculate the starting index of the current page
//        var endIndex = startIndex + itemsPerPage; // Calculate the ending index of the current page
//        var currentPageData = data.slice(startIndex, endIndex); // Get the data for the current page
//        for (var i in currentPageData) {
//            var e = currentPageData[i];
//            var buttonHtml = ' <button onclick="displayEditBrand(' + e.id + ')">edit</button>';
//            var truncatedFields = {};
//            Object.keys(e).forEach(function(key) {
//                var value = e[key];
//                if (typeof value === 'string' && value.length > 15) {
//                    value = value.substring(0, 15) + "...";
//                }
//                truncatedFields[key] = value;
//            });
//            var row = '<tr>' +
//                '<td>' + truncatedFields.id + '</td>' +
//                '<td>' + truncatedFields.brand + '</td>' +
//                '<td>' + truncatedFields.category + '</td>' +
//                '<td>' + buttonHtml + '</td>' +
//                '</tr>';
//
//            $tbody.append(row);
//        }
//        // Update the pagination buttons
//        updatePagination(totalPages);
//    }
//
//    // Update the pagination buttons
//    function updatePagination(totalPages) {
//        var $paginationContainer = $('#pagination-container');
//        $paginationContainer.empty();
//
//        for (var i = 1; i <= totalPages; i++) {
//            var buttonHtml = '<button onclick="changePage(' + i + ')">' + i + '</button>';
//            $paginationContainer.append(buttonHtml);
//        }
//    }
//    window.changePage = function(pageNumber) {
//        currentPage = pageNumber;
//        displayPage(data);
//    };
//}
//
//function displayEditBrand(id) {
//    var url = getBrandUrl() + "/" + id;
//    $.ajax({
//        url: url,
//        type: 'GET',
//        success: function(data) {
//            displayBrand(data);
//        },
//        error: handleAjaxError
//    });
//}
//
//function resetUploadDialog() {
//    //Reset file name
//    var $file = $('#brandFile');
//    $file.val('');
//    $('#brandFileName').html("Choose File");
//    //Reset various counts
//    processCount = 0;
//    fileData = [];
//    errorData = [];
//    //Update counts
//    updateUploadDialog();
//}
//
//function updateUploadDialog() {
//    $('#rowCount').html("" + fileData.length);
//    $('#processCount').html("" + processCount);
//    $('#errorCount').html("" + errorData.length);
//}
//
//function updateFileName() {
//    var $file = $('#brandFile');
//    var fileName = $file.val();
//    $('#brandFileName').html(fileName);
//}
//
//function displayUploadData() {
//    resetUploadDialog();
//    $('#upload-brand-modal').modal('toggle');
//}
//
//function displayBrand(data) {
//    $("#brand-edit-form input[name=brand]").val(data.brand);
//    $("#brand-edit-form input[name=category]").val(data.category);
//    $("#brand-edit-form input[name=id]").val(data.id);
//    $('#edit-brand-modal').modal('toggle');
//}
//
////function togglePlaceholder() {
////    var inputElement = document.getElementByTagName("input");
////    inputElement.value = "";
////}
//
//
////INITIALIZATION CODE
//function init() {
//    $('#add-brand').click(addBrand);
//    $('#update-brand').click(updateBrand);
//    $('#refresh-data').click(getBrandList);
//    //    $('#refresh-data').click(togglePlaceholder);
//    $('#upload-data').click(displayUploadData);
//    $('#process-data').click(processData);
//    $('#download-errors').click(downloadErrors);
//    $('#brandFile').on('change', updateFileName)
//}
//
//$(document).ready(init);
//$(document).ready(getBrandList);