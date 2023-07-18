function getOrderUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/order";
}

function getOrderItemUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/orderItem";
}

function getInvoiceUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content");
    return baseUrl + "/api/invoice";
}


//BUTTON ACTIONS
function addOrder(event) {
    //Set the values to update
    var $form = $("#order-form");
    var json = toJson($form);
    var url = getOrderUrl();

    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            getOrderList();
        },
        error: handleAjaxError
    });
    return false;
}

function updateOrder(event) {
    $('#edit-order-modal').modal('toggle');
    //Get the ID
    var id = $("#order-edit-form input[name=id]").val();
    var url = getOrderUrl() + "/" + id;

    //Set the values to update
    var $form = $("#order-edit-form");
    var json = toJson($form);

    $.ajax({
        url: url,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            getOrderList();
        },
        error: handleAjaxError
    });
    return false;
}


function getOrderList() {
    var url = getOrderUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayOrderList(data);
        },
        error: handleAjaxError
    });
}

function deleteOrder(id) {
    var url = getOrderUrl() + "/" + id;

    $.ajax({
        url: url,
        type: 'DELETE',
        success: function(data) {
            getOrderList();
        },
        error: handleAjaxError
    });
}

function viewOrder(id,status) {
    var url = getOrderItemUrl() + "/view/" + id;
    console.log("get called");
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            viewOrderList(data,status);
            console.log("get successful");
        },
        error: handleAjaxError
    });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData() {
    var file = $('#orderFile')[0].files[0];
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
    var url = getOrderUrl();

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
// Function to pad single-digit numbers with leading zero
function padZero(number) {
    return number.toString().padStart(2, '0');
}

function displayOrderList(data) {
    var $tbody = $('#order-table').find('tbody');
    $tbody.empty();
    for (var i in data) {
        var e = data[i];
        console.log(e);
        var date = e.dateTime;
        var formattedDatetime = date.slice(2, 3).join('-') + '-' +
            padZero(date[1]) + '-' + date[0] + ' ' +
            padZero(date[3]) + ':' + padZero(date[4]) + ':' +
            padZero(date[5]);

        var buttonHtml1 = '<button type="button" class="btn btn-secondary" data-toggle="modal" data-target="#exampleModal" onclick="viewOrder(' + e.id + ','+e.status+')" >View Order</button>'
        var buttonHtml2 = ' <button type="button" class="btn btn-secondary" onclick="printOrder(' + e.id + ')">Invoice</button>'

        var row = '<tr>' +
            '<td>' + e.id + '</td>' +
            '<td>' + formattedDatetime + '</td>' +
            '<td>' + buttonHtml1 + '</td>' +
            '<td>' + buttonHtml2 + '</td>' +
            '</tr>';
        $tbody.append(row);
    }
}

function viewOrderList(data,status) {
    var $tbody = $('#view-order-table').find('tbody');
    $tbody.empty();
    var totalSellingPrice = 0;
    for (var i in data) {
        var e = data[i];
        console.log(e);
        console.log("e.id=");
        console.log(e.id);
        var buttonHtml1 = '<button type="button" class="btn btn-secondary"';
        if (status == false) {
          buttonHtml1 += ' onclick="updateOrderItem(' + e.id + ',' + e.orderId + ',' + e.quantity + ','+status+')"';
        } else {
          buttonHtml1 += ' disabled';
        }
        buttonHtml1 += '>Edit</button>';

        var buttonHtml2 = '<button type="button" class="btn btn-secondary"';
                if (status == false) {
                  buttonHtml2 += ' onclick="updateOrderItem(' + e.id + ',' + e.orderId + ',' + e.quantity + ','+status+')"';
                } else {
                  buttonHtml2 += ' disabled';
                }
                buttonHtml2 += '>Delete</button>';

//        var buttonHtml2 = '<button type="button" class="btn btn-secondary">Delete</button>'
        var rowid = "row-" + e.id;
        var row = '<tr id=' + rowid + '>' +
            '<td>' + e.barcode + '</td>' +
            '<td>' + e.productName + '</td>' +
            '<td><input type="number" class="form-control" name="quantity" placeholder="enter product Id" value="' + e.quantity + '"></td>' +
            '<td><input type="number" inputmode="decimal" class="form-control" name="sellingPrice" placeholder="enter sellingPrice" value="' + e.sellingPrice + '"></td>' +
            '<td>' + e.sellingPrice * e.quantity + '</td>' +
            '<td>' + buttonHtml1 + '</td>' +
            '<td>' + buttonHtml2 + '</td>' +
            '</tr>';
        $tbody.append(row);
        //        console.log("row is",row);
        //        console.log("viewOrderList rowid= ",rowid);
        totalSellingPrice += (e.sellingPrice * e.quantity);
    }
    var $sellingPrice = $('#modal-footer').find('h6');
    $sellingPrice.text('Total Selling Price : ' + totalSellingPrice);
}

function displayEditOrder(id) {
    var url = getOrderUrl() + "/" + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayOrder(data);
        },
        error: handleAjaxError
    });
}

function resetUploadDialog() {
    //Reset file name
    var $file = $('#orderFile');
    $file.val('');
    $('#orderFileName').html("Choose File");
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
    var $file = $('#orderFile');
    var fileName = $file.val();
    $('#orderFileName').html(fileName);
}

function displayUploadData() {
    resetUploadDialog();
    $('#upload-order-modal').modal('toggle');
}

function displayOrder(data) {
    $("#order-edit-form input[name=dateTime]").val(data.dateTime);
    $("#order-edit-form input[name=id]").val(data.id);
    $('#edit-order-modal').modal('toggle');
}

function printOrder(id) {
    window.location.href = getInvoiceUrl() + "/" + id;
    console.log("in print order");
    updateOrder(id);
}
function updateOrder(id) {
    var url = getOrderUrl() + "/" + id;
    console.log(url);
    $.ajax({
        url: url,
        type: 'PUT',
//        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            getOrderList();
            console.log("update called successfully.");
        },
        error: handleAjaxError
    });
    return false;
}

function updateOrderItem(id, orderId, prevQty,status) {
    var rowid = '#row-' + id;
    var $row = $(rowid);
    // Find the input fields within the row and get their values
    var quantity = $row.find('td input[name="quantity"]').val();
    var sellingPrice = $row.find('td input[name="sellingPrice"]').val();
    console.log("Quantity: " + quantity - prevQty);
    console.log("Selling Price: " + sellingPrice);

    //Set the values to update
    var json = {
        "orderId": "",
        "productId": "",
        "quantity": quantity,
        "sellingPrice": sellingPrice,
        "id": id,
        "inventoryQty": quantity - prevQty
    };
    console.log("json is: ", json);

    var url = getOrderItemUrl() + "/" + id;
    $.ajax({
        url: url,
        type: 'PUT',
        data: JSON.stringify(json),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            console.log("updated");
            console.log("id=", id);
            viewOrder(orderId,status);
        },
        error: handleAjaxError
    });

    return false;
}
function updateInventory(id){
    var url = getOrderUrl() + "/" + id;
    var json={
            "orderId": "",
            "productId": "",
            }

    $.ajax({
        url: url,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            getOrderList();
        },
        error: handleAjaxError
    });
    return false;
}

function deleteOrderItem(id, orderId, prevQty) {
    var rowid = '#row-' + id;
        var $row = $(rowid);
        // Find the input fields within the row and get their values
        var quantity = $row.find('td input[name="quantity"]').val();
        var sellingPrice = $row.find('td input[name="sellingPrice"]').val();
        console.log("Quantity: " + quantity - prevQty);
        console.log("Selling Price: " + sellingPrice);

        //Set the values to update
        var json = {
            "orderId": "",
            "productId": "",
            "quantity": quantity,
            "sellingPrice": sellingPrice,
            "id": id,
            "inventoryQty": quantity - prevQty
        };
        console.log("json is: ", json);

        var url = getOrderItemUrl() + "/" + id;
        $.ajax({
            url: url,
            type: 'DELETE',
            data: JSON.stringify(json),
            headers: {
                'Content-Type': 'application/json'
            },
            success: function(response) {
                console.log("updated");
                console.log("id=", id);
                viewOrder(orderId,status);
            },
            error: handleAjaxError
        });
    return false;
}

function displayOrderItem(data) {
    $("#orderItem-edit-form input[name=orderId]").val(data.orderId);
    $("#orderItem-edit-form input[name=productId]").val(data.productId);
    $("#orderItem-edit-form input[name=quantity]").val(data.quantity);
    $("#orderItem-edit-form input[name=sellingPrice]").val(data.sellingPrice);
    $("#orderItem-edit-form input[name=id]").val(data.id);
    $('#edit-orderItem-modal').modal('toggle');
}

//INITIALIZATION CODE
function init() {
    $('#add-order').click(addOrder);
    $('#update-order').click(updateOrder);
    //	$('#update-orderItem').click(updateOrderItem);
    $('#refresh-data').click(getOrderList);
    $('#upload-data').click(displayUploadData);
    $('#process-data').click(processData);
    $('#download-errors').click(downloadErrors);
    $('#orderFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getOrderList);