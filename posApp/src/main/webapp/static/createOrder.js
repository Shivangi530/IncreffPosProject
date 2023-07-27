function getCreateOrderUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/createOrder";
}
function getCreateOrderUrl2() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/createOrder/all";
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
    // Retrieve the create order data from the array
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
        return;
    }
    if(json1.sellingPrice === ""){
        warning("Mrp cannot be empty");
        return false;
    }
    if(json1.sellingPrice <=0){
        warning("Mrp should be positive");
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
    $('#edit-createOrder-modal').modal('toggle');
    //Get the ID
    var id = $("#createOrder-edit-form input[name=id]").val();

    //Set the values to update
    var $form = $("#createOrder-edit-form");

    var json = toJson($form);
    json = JSON.parse(json);
    if (initialChecks(json) === false) {
       return false;
    }
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
        success("Object updated.")
        console.log("Object with ID " + id + " is updated:");
        console.log(objectToUpdate);
    } else {
        console.log("Object with ID " + id + " not found.");
    }
    $.ajax({
        url: url,
        type: 'POST',
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

function displaySuccessCreateOrder(event) {
    var url = getCreateOrderUrl();
    $('#success-createOrder-modal').modal('toggle');
}

function checkCreateOrder() {
    var url = getCreateOrderUrl2();
    var data = createOrderData;
    console.log("The data is: ", data);
    // Find the object with the matching ID
    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(createOrderData),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            getCreateOrderList();
            return true;
        },
        error:function(response) {
             handleAjaxError;
             return false;
        }
    });
    return false;
}

function OrderItemFunction(json2) {
    //Set the values to update
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    var urlOrderItem = baseUrl + "/api/orderItem";
    console.log("placing the order");
    //  Function call to enter data into orderItem.
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

function createOrder() {
    //Set the values to update
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    var urlOrder = baseUrl + "/api/order";
    var generatedId;

    if(createOrderData.length==0){
        danger("Cannot place empty order");
        return;
    }

    // FUNCTION
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

            //FUNCTION
            var json2 = createOrderData;
            console.log("createOrderData", createOrderData);
            json2.forEach(function(e) {
                e.orderId = generatedId;
                console.log(e);
                OrderItemFunction(e);
            });
            createOrderData = [];
            getCreateOrderList();
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

function placeOrder(event) {
    if(createOrderData.length==0){
        danger("Cannot place empty order");
        return;
    }
    var url = getCreateOrderUrl2();
    var data = createOrderData;
        console.log("The data is: ", data);
        // Find the object with the matching ID
        $.ajax({
            url: url,
            type: 'POST',
            data: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            },
            success: function(response) {
            console.log("success in place order "+url);

                getCreateOrderList();
                createOrder();
                return true;
            },
            error:function(response) {
            console.log("error in place order "+url);
            console.log(response);
            console.log(response.responseJSON.message);
            danger(response.responseJSON.message);
                 handleAjaxError;
                 return false;
            }
        });
        return false;
}

function redirectToViewOrder() {
    window.location.href = getOrderUrl2();
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
            console.log("update called successfully.");
        },
        error: handleAjaxError
    });
    return false;
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
    parsedArray.reverse();
    var $tbody = $('#createOrder-table').find('tbody');
    $tbody.empty();
    console.log(parsedArray);
    // Accessing the parsed objects
    parsedArray.forEach(function(e,index) {
        if (!e.id) {
            e.id = generateUniqueId();
        }
        e.sellingPrice=parseFloat(e.sellingPrice).toFixed(2);
        console.log("e.id");
        console.log(e.id);
        console.log(e.barcode);
        console.log(e.quantity);
        console.log(e.sellingPrice);
        var buttonHtml = '<button class="btn btn-outline-primary" onclick="deleteCreateOrder(' + index + ')">Delete</button>'
        buttonHtml += ' <button class="btn btn-outline-primary" onclick="displayEditCreateOrder(' + index + ')">Edit</button>'

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
    console.log(createOrderData);
    console.log("id=",id);
    console.log(createOrderData[0]);
    console.log(createOrderData[0].barcode);
    console.log("The edit button");
    var data=createOrderData[id];
    if (!data) {
        console.error('Item not found at index:', id);
        return;
    }
    displayCreateOrder(data);
}

function deleteCreateOrder(index) {
    console.log("The delete button");
    console.log("index=",index);
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