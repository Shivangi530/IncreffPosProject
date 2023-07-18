function getCreateOrderUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/createOrder";
}
var createOrderData = [];

function getCreateOrderList() {
    // Retrieve the create order data from the array
    var data = createOrderData;
    displayCreateOrderList(data);
}

//BUTTON ACTIONS
function addCreateOrder(event) {
    //Set the values to update
    var $form = $("#createOrder-form");
    var json = toJson($form);
    var json1 = JSON.parse(json);
    var url = getCreateOrderUrl();
    var data = createOrderData;
    console.log("data is: ", data);

    for (var i = 0; i < data.length; i++) {
        if (data[i].barcode === json1.barcode) {
            json1.quantity = parseInt(json1.quantity) + parseInt(data[i].quantity);
            break;
        }
    }
    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(json1),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            var pa = json1;
            pa.id = generateUniqueId();
            console.log("json id= ", pa.id);
            for (var i = 0; i < createOrderData.length; i++) {
                if (createOrderData[i].barcode === pa.barcode) {
                    createOrderData.splice(i, 1);
                    break;
                }
            }
            createOrderData.push(pa);
            getCreateOrderList();
            resetFormFields($form);
        },
        error: handleAjaxError
    });
    return false;
}

function showError(message) {
    errorContainer.textContent = message; // Update the error message content
    errorContainer.style.display = 'block'; // Show the error message element
}

function updateCreateOrder(event) {
    var url = getCreateOrderUrl();
    $('#edit-createOrder-modal').modal('toggle');
    //Get the ID
    var id = $("#createOrder-edit-form input[name=id]").val();

    //Set the values to update
    var $form = $("#createOrder-edit-form");
    var json = toJson($form);
    json = JSON.parse(json);
    var data = createOrderData;
    console.log("The data is: ", data);
    // Find the object with the matching ID
    var objectToUpdate = data.find(function(obj) {
        console.log("obj id= ", obj.id, "id = ", id);
        return obj.id === id;
    });
    if (objectToUpdate) {
        // Update the properties of the object
        objectToUpdate.barcode = json.barcode;
        objectToUpdate.quantity = json.quantity;
        objectToUpdate.sellingPrice = json.sellingPrice;

        console.log("Object with ID " + id + " is updated:");
        console.log(objectToUpdate);
    } else {
        console.log("Object with ID " + id + " not found.");
    }
    $.ajax({
        url: url,
        type: 'PUT',
        data: JSON.stringify(objectToUpdate),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {

            getCreateOrderList();
        },
        error: handleAjaxError
    });

    return false;
}


function OrderItemFunction(json2) {
    //Set the values to update
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    var urlOrderItem = baseUrl + "/api/orderItem";
    console.log("placing the order");
    //////  Function call to enter data into orderItem.
    $.ajax({
        url: urlOrderItem,
        type: 'POST',
        data: JSON.stringify(json2),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            success("Order Placed!");
            console.log("Order Item called successfully");
            //	   		getCreateOrderList();
        },
        error: handleAjaxError
    });

    return false;
}

function placeOrder(event) {
    //Set the values to update
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    var urlOrder = baseUrl + "/api/order";
    var generatedId;
    console.log("placing the order");
    //OrderForm form
    var json;
    $.ajax({
        url: urlOrder,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            console.log(response);
            generatedId = response;
            console.log("generatedId= ", generatedId);
            console.log("Created order successfully");
            var json2 = createOrderData;
            console.log("createOrderData", createOrderData);
            json2.forEach(function(e) {
                e.orderId = generatedId;
                console.log(e);
                OrderItemFunction(e);
            });
            createOrderData = [];
            getCreateOrderList();
        },
        error: handleAjaxError
    });

    return false;
}
// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData() {
    var file = $('#createOrderFile')[0].files[0];
    readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results) {
    fileData = results.data;
    uploadRows();
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
    var url = getCreateOrderUrl();

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

// Function to generate a unique ID
var lastAssignedId = 0;

function generateUniqueId() {
    // Generate a random number or use a library for generating unique IDs
    lastAssignedId++;
    return lastAssignedId.toString();
}
//UI DISPLAY METHODS

function displayCreateOrderList(parsedArray) {
    var $tbody = $('#createOrder-table').find('tbody');
    $tbody.empty();
    console.log(parsedArray);
    //        var parsedArray = data.map(function(jsonString) {
    //            return JSON.parse(jsonString);
    //       });
    // Accessing the parsed objects
    parsedArray.forEach(function(e) {
        if (!e.id) {
            e.id = generateUniqueId();
        }
        console.log("e.id");
        console.log(e.id);
        console.log(e.barcode);
        console.log(e.quantity);
        console.log(e.sellingPrice);

        var buttonHtml = '<button onclick="deleteCreateOrder(' + e.id + ')">delete</button>'
        buttonHtml += ' <button onclick="displayEditCreateOrder(' + e.id + ')">edit</button>'

        var row = '<tr>' +
            '<td>' + e.barcode + '</td>' +
            '<td>' + e.quantity + '</td>' +
            '<td>' + e.sellingPrice + '</td>' +
            '<td>' + buttonHtml + '</td>' +
            '</tr>';
        $tbody.append(row);
    });
}

function displayEditCreateOrder(id) {
    var url = getCreateOrderUrl();
    var id = id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            console.log(data.id);
            data.id = id;
            displayCreateOrder(data);
        },
        error: handleAjaxError
    });
}

function deleteCreateOrder(id) {
    console.log("The delete button");
    var url = getCreateOrderUrl();
    var data = createOrderData;
    console.log("The data is: ", data);
    var idString = id.toString();
    var index = createOrderData.findIndex(function(obj) {
        console.log("obj id= ", obj.id, "id = ", idString);
        return obj.id === idString;
    });

    if (index !== -1) {
        // Remove the create order object from the array
        createOrderData.splice(index, 1);
        console.log("Order with ID " + idString + " is deleted.");
    } else {
        console.log("Order with ID " + idString + " not found.");
    }
    getCreateOrderList();
}

function resetUploadDialog() {
    //Reset file name
    var $file = $('#createOrderFile');
    $file.val('');
    $('#createOrderFileName').html("Choose File");
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
    var $file = $('#createOrderFile');
    var fileName = $file.val();
    $('#createOrderFileName').html(fileName);
}

function displayUploadData() {
    resetUploadDialog();
    $('#upload-createOrder-modal').modal('toggle');
}

function displayCreateOrder(data) {
    $("#createOrder-edit-form input[name=id]").val(data.id);
    $("#createOrder-edit-form input[name=barcode]").val(data.barcode);
    $("#createOrder-edit-form input[name=quantity]").val(data.quantity);
    $("#createOrder-edit-form input[name=sellingPrice]").val(data.sellingPrice);
    $('#edit-createOrder-modal').modal('toggle');
}


//INITIALIZATION CODE
function init() {
    $('#add-createOrder').click(addCreateOrder);
    $('#update-createOrder').click(updateCreateOrder);
    $('#refresh-data').click(getCreateOrderList);
    $('#upload-data').click(displayUploadData);
    $('#process-data').click(processData);
    $('#download-errors').click(downloadErrors);
    $('#placeOrder').click(placeOrder);
    $('#createOrderFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getCreateOrderList);