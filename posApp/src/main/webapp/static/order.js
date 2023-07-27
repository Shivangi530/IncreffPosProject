var table;

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
function getOrderList() {
    var url = getOrderUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
//            data=data.reverse();
//            totalItems = data.length;
            console.log(data);
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

function viewOrder(id, status) {
    var url = getOrderItemUrl() + "/view/" + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            console.log("data.length(): ",data.length);
            if(data.length==0){

                updateOrder(id,"deleted");
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
    var $tbody = $('#order-table').find('tbody');
    table.clear().draw();
    var dataRows=[];
    for (var i in data) {
        var e = data[i];
        var date = e.dateTime;
        var formattedDatetime = date.slice(2, 3).join('-') + '-' +
                    padZero(date[1]) + '-' + date[0] + ' ' +
                    padZero(date[3]) + ':' + padZero(date[4]) + ':' +
                    padZero(date[5]);
        console.log("displayOrderList",e.status);
        var status=e.status;
        var buttonHtml1 = '<button type="button" class="btn ';
         if (status === "created") {
                    buttonHtml1 += 'btn-outline-primary" ';
                } else if (status === "invoiced") {
                    buttonHtml1 += 'btn-outline-success" ';
                } else if (status === "deleted") {
                    buttonHtml1 += 'btn-outline-dark" ';
                } else {
                    buttonHtml1 += 'btn-outline-secondary" '; // Default class if status is not recognized
                }

        if (e.status === "deleted") {
            buttonHtml1 += ' disabled';
        } else {
            buttonHtml1 += ' data-toggle="modal" data-target="#exampleModal" onclick="viewOrder(' + e.id + ',\'' + e.status + '\')"';
        }
        buttonHtml1 += '>View Order</button>';

        var buttonHtml2 = '<button type="button" class="btn ';
        if (status === "created") {
            buttonHtml2 += 'btn-outline-primary" ';
        } else if (status === "invoiced") {
            buttonHtml2 += 'btn-outline-success" ';
        } else if (status === "deleted") {
            buttonHtml2 += 'btn-outline-dark" ';
        } else {
            buttonHtml2 += 'btn-outline-secondary" '; // Default class if status is not recognized
        }
        if (e.status === "deleted") {
            buttonHtml2 += ' disabled';
        } else {
            buttonHtml2 += ' onclick="printOrder(' + e.id + ', \'' + e.status + '\')"';
        }
        buttonHtml2 += '>Invoice</button>';

        var row = '<tr>' +
            '<td>' + e.id + '</td>' +
            '<td>' + formattedDatetime + '</td>' +
            '<td>' + e.status + '</td>' +
            '<td>' + buttonHtml1 + '</td>' +
            '<td>' + buttonHtml2 + '</td>' +
            '</tr>';
        console.log(e.id, formattedDatetime, e.status);
        dataRows.push([e.id, formattedDatetime, e.status, buttonHtml1,buttonHtml2]);
    }
    table.rows.add(dataRows).draw();
}

function viewOrderList(data, status) {
console.log("in vieworderlist: ",status);
//    var $header = $('#exampleModalLabel');
//    $header.append(":" + data[0].orderId);
    var $tbody = $('#view-order-table').find('tbody');
    $tbody.empty();
    var totalSellingPrice = 0;
    for (var i in data) {
        var e = data[i];
        console.log(e);
        var buttonHtml1 = '<button type="button" class="btn btn-outline-primary" ';

        if (status === "created") {
            buttonHtml1 += ' onclick="updateOrderItem(' + e.id + ',' + e.orderId + ',' + e.quantity + ',\'' + status + '\')"';
        } else {
            buttonHtml1 += ' disabled';
        }
        buttonHtml1 += '>Edit</button>';


        var buttonHtml2 = '<button type="button" class="btn btn-outline-primary" ';

        if (status === "created") {
            buttonHtml2 += ' onclick="deleteOrderItem(' + e.id + ',' + e.orderId + ',' + e.quantity + ',\'' + status + '\')"';
        } else {
            buttonHtml2 += ' disabled';
        }
        buttonHtml2 += '>Delete</button>';

        console.log(buttonHtml1);

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

function displayOrder(data) {
    $("#order-edit-form input[name=dateTime]").val(data.dateTime);
    $("#order-edit-form input[name=id]").val(data.id);
    $('#edit-order-modal').modal('toggle');
}

function printOrder(id) {
    window.location.href = getInvoiceUrl() + "/" + id;
    console.log("in print order");
    updateOrder(id,"invoiced");
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
    console.log("updateOrderItem: ",status );
    var rowid = '#row-' + id;
    var $row = $(rowid);
    // Find the input fields within the row and get their values
    var quantity = $row.find('td input[name="quantity"]').val();
    var sellingPrice = $row.find('td input[name="sellingPrice"]').val();
    console.log("Quantity: " + quantity - prevQty);
    console.log("Selling Price: " + sellingPrice);
    if(quantity==""){
        warning("Quantity cannot be empty");
        return false;
    }
    if(parseFloat(quantity)-parseInt(quantity)>0){
        warning("Quantity should be of integer type");
        return false;
    }
    if(quantity>1000000000){
        danger("Quantity entered is too large");
        return false;
    }
    if(sellingPrice==""){
            warning("Selling Price cannot be empty");
            return false;
        }
    if(sellingPrice>1000000000){
        danger("Selling Price entered is too large");
        return false;
    }


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
            console.log("deleted");
            console.log("id=", id);
            viewOrder(orderId, status);
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
    $('#update-orderItem').click(updateOrderItem);
    $('#refresh-data').click(getOrderList);
    getOrderList();
        table = $('#order-table').DataTable({
          columnDefs: [
            { targets: [3,4], orderable: false },
            { targets: [0, 1, 2, 3,4], className: "text-center" }
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