function getCreateOrderUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/validate-order-item";
}
function getOrderUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/order";
}
function getOrderUrl2() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/ui/order";
}
function getInvoiceUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content");
    return baseUrl + "/api/invoice";
}

var createOrderData = [];

function getCreateOrderList() {
    var data = createOrderData;
    displayCreateOrderList(data);
}

function initialChecks(json1){
    if(json1.barcode === ""){
        warning("Barcode cannot be empty");
        return false;
    }
    if(json1.quantity === ""){
        warning("Quantity cannot be empty");
        return false;
    }
    if(parseFloat(json1.quantity)-parseInt(json1.quantity)>0){
        danger("Quantity should be of integer type");
        return false;
    }
    if(json1.sellingPrice === ""){
        warning("Mrp cannot be empty");
        return false;
    }
    if(json1.sellingPrice < 0.01){
        warning("Mrp should not be less than 0.01");
        return false;
    }
    if(json1.quantity > 10000000){
        warning("Quantity is too large");
        return false;
    }
    if(json1.sellingPrice > 10000000){
        danger("Selling Price is too large");
        return false;
    }
}

//BUTTON ACTIONS
function addCreateOrder(event) {
    //Set the values to update
    var $form = $("#createOrder-form");
    var json = toJson($form);
    var json1 = JSON.parse(json);
    json1.barcode= json1.barcode.toLowerCase().trim();
    if (initialChecks(json1) === false) {
        return false;
    }
    var url = getCreateOrderUrl();
    var data = createOrderData;

    for (var i = 0; i < data.length; i++) {
        if (data[i].barcode === json1.barcode) {
            if(data[i].sellingPrice=== json1.sellingPrice){
                json1.quantity = parseInt(json1.quantity) + parseInt(data[i].quantity);
            }else{
                warning(`Item already present with selling price: ${data[i].sellingPrice}`);
                return false;
            }
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
            var flag=false;
            for (var i = 0; i < createOrderData.length; i++) {
                if (createOrderData[i].barcode === pa.barcode) {
                    createOrderData[i].quantity= pa.quantity;
                    flag=true;
                    break;
                }
            }
            if(flag==false){
                createOrderData.push(pa);}
            getCreateOrderList();
            resetFormFields($form);
        },
        error: handleAjaxError
    });
    return false;
}

function updateCreateOrder(event) {
    var url = getCreateOrderUrl();
    var id = $("#createOrder-edit-form input[name=id]").val();
    var $form = $("#createOrder-edit-form");
    var json = toJson($form);
    json = JSON.parse(json);
    if (initialChecks(json) === false) {
       return false;
    }
    var data = createOrderData;
    var objectToUpdate = data.find(function(obj) {
        return obj.id === id;
    });

    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(json),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            if (objectToUpdate) {
                // Update the properties of the object
                objectToUpdate.barcode = json.barcode;
                objectToUpdate.quantity = json.quantity;
                objectToUpdate.sellingPrice = json.sellingPrice;
            }
            $('#edit-createOrder-modal').modal('toggle');
            success("Object updated.");
            getCreateOrderList();
        },
        error: handleAjaxError
    });
    return false;
}

function placeOrder(event) {
    if(createOrderData.length==0){
        danger("Cannot place empty order");
        return;
    }

    var data = createOrderData;
    console.log(data);
    $.ajax({
        url: getOrderUrl(),
        type: 'POST',
        data: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            success("Order placed.")
            createOrderData = [];
            getCreateOrderList();
            var generatedId=response;
            $('#success-createOrder-modal').modal('toggle');
            $('#generateInvoice').click(function() {
                 printOrder(generatedId)});
            $('#redirectToViewOrder').click(function() {
                 redirectToViewOrder()});
        },
        error: handleAjaxError
    });

    return false;
}

function redirectToViewOrder() {
    window.location.href = getOrderUrl2();
}
function printOrder(id) {
    updateOrderStatus(id,"INVOICED");
    window.location.href = getInvoiceUrl() + "/" + id;
}

function updateOrderStatus(id,status) {
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
            console.log("update called successfully.");
        },
        error: handleAjaxError
    });
    return false;
}

// Function to generate a unique ID
var lastAssignedId = 0;
function generateUniqueId() {
    lastAssignedId++;
    return lastAssignedId.toString();
}

//UI DISPLAY METHODS

function displayCreateOrderList(parsedArray) {
    parsedArray.reverse();
    var $tbody = $('#createOrder-table').find('tbody');
    var $thead = $('#createOrder-table').find('thead');
    var $placeOrderButton =$('#placeOrder')
    $tbody.empty();
    if (parsedArray.length === 0) {
        $thead.hide();
        $placeOrderButton.hide();
    } else {
        $thead.show();
        $placeOrderButton.show();
    }
    // Accessing the parsed objects
    parsedArray.forEach(function(e,index) {
        if (!e.id) {
            e.id = generateUniqueId();
        }
        e.sellingPrice=parseFloat(e.sellingPrice).toFixed(2);
        var buttonHtml = ' <button class="btn btn-outline-primary" onclick="displayEditCreateOrder(' + index + ')">Edit</button>&nbsp;'
        buttonHtml += '<button class="btn btn-outline-danger" onclick="deleteCreateOrder(' + index + ')">Delete</button>'

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
    var data=createOrderData[id];
    if (!data) {
        console.error('Item not found at index:', id);
        return;
    }
    displayCreateOrder(data);
}

function deleteCreateOrder(index) {
    var data=createOrderData[index];
        if (!data) {
            console.error('Item not found at index:', index);
            return;
        }
    // Remove the element at the specified index
    createOrderData.splice(index, 1);
    getCreateOrderList();
}

function refresh() {
    getCreateOrderList();
    resetFormFields($('#createOrder-form'));
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
    $('#refresh-data').click(refresh);
    $('#placeOrder').click(placeOrder);
}

$(document).ready(init);
$(document).ready(getCreateOrderList);