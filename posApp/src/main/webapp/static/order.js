var table;

function getOrderUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/order";
}

function getOrderItemUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/order-item";
}

function getInvoiceUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content");
    return baseUrl + "/api/invoice";
}

//BUTTON ACTIONS
function getOrderList() {
    var url = getOrderUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            console.log(data);
            displayOrderList(data);
        },
        error: handleAjaxError
    });
}

function viewOrder(id, status) {
    var url = getOrderItemUrl() + "/view/" + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            console.log("data.length(): ",data.length);
            if(data.length==0){
                updateOrder(id,"CANCELED");
            }
            data=data.reverse();
            console.log("in view order:",status);
            var $header = $('#exampleModalLabel');
            $header.text("Order :" + id);
            viewOrderList(data, status);
        },
        error: handleAjaxError
    });
}

//UI DISPLAY METHODS
// Function to pad single-digit numbers with leading zero
function padZero(number) {
    return number.toString().padStart(2, '0');
}

function displayOrderList(data) {
    console.log(data);
    var $tbody = $('#order-table').find('tbody');
    table.clear().draw();
    var dataRows=[];
    for (var i in data) {
        var e = data[i];
        var date = e.dateTime;
        var formattedDatetime = date.slice(2, 3).join('-') + '/' +
                    padZero(date[1]) + '/' + date[0] + ' ' +
                    padZero(date[3]) + ':' + padZero(date[4]) + ':' +
                    padZero(date[5]);
        console.log("displayOrderList",e.status);
        var status=e.status;
        var buttonHtml1 = '<button type="button" class="btn ';
         if (status === "CREATED") {
                    buttonHtml1 += 'btn-outline-primary" ';
                } else if (status === "INVOICED") {
                    buttonHtml1 += 'btn-outline-success" ';
                } else if (status === "CANCELED") {
                    buttonHtml1 += 'btn-outline-dark " ';
                } else {
                    buttonHtml1 += 'btn-outline-secondary " ';
                }

        if (e.status === "CANCELED") {
            buttonHtml1 += ' disabled';
        } else {
            buttonHtml1 += ' data-toggle="modal" data-target="#exampleModal" onclick="viewOrder(' + e.id + ',\'' + e.status + '\')"';
        }
        buttonHtml1 += '>View Order</button>&nbsp;';

        if (status === "CREATED") {
            buttonHtml1 += '<button type="button" class="btn btn-outline-primary" onclick="printOrder(' + e.id + ', \'' + e.status + '\')">Download Invoice</button>';
        } else if (status === "INVOICED") {
            buttonHtml1 += '<button type="button" class="btn btn-outline-success" onclick="printOrder(' + e.id + ', \'' + e.status + '\')">Download Invoice</button>';
        } else if (status === "CANCELED") {
            buttonHtml1 += '<button type="button" class="btn btn-outline-dark" disabled>Download Invoice</button>';
        } else {
            buttonHtml1 += '<button type="button" class="btn btn-outline-secondary d-none" disabled></button>'; // Default class if status is not recognized
        }

        console.log(e.id, formattedDatetime, e.status);
        dataRows.push([e.id, formattedDatetime, e.status, buttonHtml1]);
    }
    table.rows.add(dataRows).draw();
}

function viewOrderList(data, status) {
    var $tbody = $('#view-order-table').find('tbody');
    $tbody.empty();
    var totalSellingPrice = 0;
    for (var i in data) {
        var e = data[i];
        console.log(e);
        var buttonHtml1 = '<button type="button" class="btn btn-outline';

        if (status === "CREATED") {
            buttonHtml1 += '-primary"  onclick="updateOrderItem(' + e.id + ',' + e.orderId + ',' + e.quantity + ',\'' + status + '\')"';
        } else {
            buttonHtml1 += '-secondary"  disabled';
        }
        buttonHtml1 += '>Edit</button>&nbsp;';

        buttonHtml1 += '<button type="button" class="btn btn-outline';

        if (status === "CREATED") {
            buttonHtml1 += '-danger"  onclick="deleteOrderItem(' + e.id + ',' + e.orderId + ',' + e.quantity + ',\'' + status + '\')"';
        } else {
            buttonHtml1 += '-secondary"  disabled';
        }
        buttonHtml1 += '>Delete</button>';
        var sellingPrice= ((e.sellingPrice * e.quantity).toFixed(2));
        var rowid = "row-" + e.id;
        var row = '<tr id=' + rowid + '>' +
            '<td>' + e.barcode + '</td>' +
            '<td>' + e.productName + '</td>' +
            '<td><input type="number" class="form-control" name="quantity" placeholder="enter product Id" value="' + e.quantity + '"></td>' +
            '<td><input type="number" inputmode="decimal" class="form-control" name="sellingPrice" placeholder="enter sellingPrice" value="' + e.sellingPrice + '"></td>' +
            '<td>' + sellingPrice + '</td>' +
            '<td>' + buttonHtml1 + '</td>' +
            '</tr>';
        $tbody.append(row);
        totalSellingPrice += (e.sellingPrice * e.quantity);
    }
    var $sellingPrice = $('#modal-footer').find('h6');
    $sellingPrice.text('Total Selling Price : ' + totalSellingPrice.toFixed(2));
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

function displayOrder(data) {
    $("#order-edit-form input[name=dateTime]").val(data.dateTime);
    $("#order-edit-form input[name=id]").val(data.id);
    $('#edit-order-modal').modal('toggle');
}

function printOrder(id) {
    window.location.href = getInvoiceUrl() + "/" + id;
    console.log("in print order");
    updateOrder(id,"INVOICED");
}

function updateOrder(id,status) {
    var url = getOrderUrl() + "/" + id;
    console.log(url);
    $.ajax({
        url: url,
        type: 'PUT',
        data: status,
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

function updateOrderItem(id, orderId, prevQty, status) {
    var rowid = '#row-' + id;
    var $row = $(rowid);
    // Find the input fields within the row and get their values
    var quantity = $row.find('td input[name="quantity"]').val();
    var sellingPrice = $row.find('td input[name="sellingPrice"]').val();
    if(quantity==""){
        warning("Quantity cannot be empty");
        return false;
    }
    if(quantity.includes('.')){
            warning("Quantity should be of integer type");
            return false;
    }
    if(parseFloat(quantity)-parseInt(quantity)>0){
        warning("Quantity should be of integer type");
        return false;
    }
    if(quantity<=0){
        warning("Quantity should be positive");
        return false;
    }
    if(quantity>10000000){
        warning("Quantity entered is too large");
        return false;
    }
    if(sellingPrice==""){
            warning("Selling Price cannot be empty");
            return false;
        }
    if(sellingPrice>10000000){
        warning("Selling Price entered is too large");
        return false;
    }
    if(sellingPrice<0.01){
        warning("Mrp should not be less than 0.01");
        return false;
    }

    //Set the values to update
    var json = {
        "orderId": "",
        "productId": "",
        "quantity": quantity,
        "sellingPrice": parseFloat(sellingPrice).toFixed(2),
        "id": id,
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
            success("Updated");
            viewOrder(orderId, status);
        },
        error: handleAjaxError
    });
    return false;
}

function updateInventory(id) {
    var url = getOrderUrl() + "/" + id;
    var json = {
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
        "id": id
    };

    var url = getOrderItemUrl() + "/" + id;
    $.ajax({
        url: url,
        type: 'DELETE',
        data: JSON.stringify(json),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            success("Deleted");
            viewOrder(orderId, status);
        },
        error: handleAjaxError
    });
    return false;
}

//INITIALIZATION CODE
function init() {
    $('#update-orderItem').click(updateOrderItem);
    $('#refresh-data').click(getOrderList);
    getOrderList();
        table = $('#order-table').DataTable({
          columnDefs: [
            { targets: [3], orderable: false },
            { targets: [0, 1, 2, 3], className: "text-center" }
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
            { searchable: true },
            { searchable: true },
            { searchable: true },
            { searchable: false }
          ],
        });
}

$(document).ready(init);