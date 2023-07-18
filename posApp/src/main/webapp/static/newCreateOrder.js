
function getCreateOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/createOrder";
}

//BUTTON ACTIONS
var createOrderData=[];

function getCreateOrderList() {
  // Retrieve the create order data from the array
  var data = createOrderData;
  displayCreateOrderList(data);
}

function addCreateOrder(event) {
  // Set the values to update
  var $form = $("#createOrder-form");
    var json = toJson($form);
    var url = getCreateOrderUrl();
    $.ajax({
      url: url,
      type: 'POST',
      data: json,
      headers: {
        'Content-Type': 'application/json'
      },
      success: function(response) {
        // Store the create order data in the array
        createOrderData.push(json);
        getCreateOrderList();
      },
      error: handleAjaxError
    });
    return false;
}

function updateCreateOrder(event){
	$('#edit-createOrder-modal').modal('toggle');
	//Get the ID
	var id = $("#createOrder-edit-form input[name=id]").val();
	var url = getCreateOrderUrl() + "/" + id;

	//Set the values to update
	var $form = $("#createOrder-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
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

function deleteCreateOrder(id){
	var url = getCreateOrderUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getCreateOrderList();
	   },
	   error: handleAjaxError
	});
}

//Place an order
function placeOrder(event) {
  // Set the values to update

    var url = getCreateOrderUrl();
    $.ajax({
      url: url,
      type: 'POST',
      data: json,
      headers: {
        'Content-Type': 'application/json'
      },
      success: function(response) {
        console.log("Order Placed");
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


function processData(){
	var file = $('#createOrderFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
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
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS
// Function to generate a unique ID
   function generateUniqueId() {
     // Generate a random number or use a library for generating unique IDs
     var randomNumber = Math.floor(Math.random() * 100000);
     return randomNumber.toString();
   }

function displayCreateOrderList(data){
	var $tbody = $('#createOrder-table').find('tbody');
	$tbody.empty();
	console.log(data);
    var parsedArray = data.map(function(jsonString) {
        return JSON.parse(jsonString);
   });

   // Generate an ID for each data entry if it doesn't exist
   parsedArray.forEach(function(parsedObject) {

   });
   // Accessing the parsed objects
   parsedArray.forEach(function(e) {
        //if (!e.id) {
          e.id = generateUniqueId();
        //}

        console.log("e.id");
        console.log(e.id);
        console.log(e.barcode);
        console.log(e.quantity);
        console.log(e.sellingPrice);
                var buttonHtml = '<button onclick="deleteCreateOrder(' + e.id + ')">delete</button>'
               buttonHtml += ' <button onclick="displayEditCreateOrder(' + e.id + ')">edit</button>'
               var row = '<tr>'
               + '<td>' + e.barcode + '</td>'
               + '<td>' + e.quantity + '</td>'
               + '<td>' + e.sellingPrice + '</td>'
               + '<td>' + buttonHtml + '</td>'
               + '</tr>';
                 $tbody.append(row);
   });
}

function displayEditCreateOrder(id){
	var url = getCreateOrderUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayCreateOrder(data);
	   },
	   error: handleAjaxError
	});
}

function resetUploadDialog(){
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

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#createOrderFile');
	var fileName = $file.val();
	$('#createOrderFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-createOrder-modal').modal('toggle');
}

function displayCreateOrder(data){
	$("#createOrder-edit-form input[name=orderId]").val(data.orderId);
	$("#createOrder-edit-form input[name=quantity]").val(data.quantity);
	$("#createOrder-edit-form input[name=sellingPrice]").val(data.sellingPrice);
	$('#edit-createOrder-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-createOrder').click(addCreateOrder);
	$('#update-createOrder').click(updateCreateOrder);
	$('#refresh-data').click(getCreateOrderList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#createOrderFile').on('change', updateFileName);
    $('#placeOrder').click(placeOrder);
}

$(document).ready(init);
$(document).ready(getCreateOrderList);

/////////////////////////////////////////////////////////////////////////////////////////////

function updateCreateOrder(event) {
  $('#edit-createOrder-modal').modal('toggle');
  // Get the ID
  var id = $("#createOrder-edit-form input[name=id]").val();
  var url = getCreateOrderUrl() + "/" + id;

  // Set the values to update
  var $form = $("#createOrder-edit-form");
  var json = toJson($form);

  // Find the create order in the local data array and update its properties
  var createOrderToUpdate = data.find(function(obj) {
    return obj.id === id;
  });

  if (createOrderToUpdate) {
    createOrderToUpdate.barcode = json.barcode;
    createOrderToUpdate.quantity = json.quantity;
    createOrderToUpdate.sellingPrice = json.sellingPrice;
  } else {
    console.log("Create order with ID " + id + " not found.");
    return false;
  }

  // Make the AJAX request to update the create order on the server
  $.ajax({
    url: url,
    type: 'PUT',
    data: JSON.stringify(createOrderToUpdate),
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


//////////////////////////////////////////////////
@RestController
@RequestMapping("/api")
public class ApiController {
    // Other methods...

    @PutMapping("/createOrder/{id}")
    public ResponseEntity<CreateOrder> updateCreateOrder(@PathVariable("id") String id, @RequestBody CreateOrder updatedOrder) {
        // Find the create order with the specified ID from a database or service
        CreateOrder existingOrder = findCreateOrderById(id);

        if (existingOrder != null) {
            // Update the properties of the existing create order
            existingOrder.setBarcode(updatedOrder.getBarcode());
            existingOrder.setQuantity(updatedOrder.getQuantity());
            existingOrder.setSellingPrice(updatedOrder.getSellingPrice());

            // Save the updated create order to a database or service
            saveCreateOrder(existingOrder);

            return ResponseEntity.ok(existingOrder); // Return the updated create order
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if the create order doesn't exist
        }
    }

    // Helper methods for database or service operations
    private CreateOrder findCreateOrderById(String id) {
        // Retrieve the create order with the specified ID from a database or service
        // Return the create order or null if not found
    }

    private void saveCreateOrder(CreateOrder createOrder) {
        // Save the create order to a database or service
    }
}

//////////////////////////////////////////////////////////////////////
function updateCreateOrder(event) {
  $('#edit-createOrder-modal').modal('toggle');
  // Get the ID
  var id = $("#createOrder-edit-form input[name=id]").val();

  // Set the values to update
  var $form = $("#createOrder-edit-form");
  var json = toJson($form);
  var createOrderToUpdate = JSON.parse(json);

  // Find the create order with the matching ID in the stored data
  var objectToUpdate = storedCreateOrderData.find(function (obj) {
    return obj.id === id;
  });

  if (objectToUpdate) {
    // Update the properties of the create order
    objectToUpdate.barcode = createOrderToUpdate.barcode;
    objectToUpdate.quantity = createOrderToUpdate.quantity;
    objectToUpdate.sellingPrice = createOrderToUpdate.sellingPrice;

    console.log("Create order with ID " + id + " is updated:");
    console.log(objectToUpdate);
  } else {
    console.log("Create order with ID " + id + " not found.");
  }

  displayCreateOrderList(storedCreateOrderData);

  return false;
}

///////////////////////////////////////////////////////////
@ApiOperation(value = "Updates a createOrder")
@RequestMapping(path = "/api/createOrder/{id}", method = RequestMethod.PUT)
public ResponseEntity<String> updateCreateOrder(@PathVariable int id, @RequestBody CreateOrderForm form) {
    CreateOrder createOrder = findCreateOrderById(id);
    if (createOrder == null) {
        // Return a 404 Not Found response if the create order is not found
        return ResponseEntity.notFound().build();
    }

    // Update the properties of the create order
    createOrder.setBarcode(form.getBarcode());
    createOrder.setQuantity(form.getQuantity());
    createOrder.setSellingPrice(form.getSellingPrice());

    // Perform any additional operations or validations here

    // Save the updated create order to the database or service

    // Return a 200 OK response
    return ResponseEntity.ok("Create order updated successfully");
}

private CreateOrder findCreateOrderById(int id) {
    // Retrieve the create order with the specified ID from a database or service
    // Return the create order or null if not found
}

int id = form.getId();
    CreateOrder createOrder = findCreateOrderById(id);
    if (createOrder == null) {
        // Return a 404 Not Found response if the create order is not found
        return ResponseEntity.notFound().build();
    }

    ///
    function getOrderUrl(){
    	var baseUrl = $("meta[name=baseUrl]").attr("content")
    	return baseUrl + "/api/report/salesReport";
    }

    function getOrderList(){
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

    //function getOrderListByFilter(){
    //	var url = getOrderUrl();
    //	var $form = $("#orderItem-form");
    //    var json = toJson($form);
    //    console.log(json);
    //    $.ajax({
    //    	   url: url,
    //    	   type: 'POST',
    //    	   data: json,
    //    	   headers: {
    //           	'Content-Type': 'application/json'
    //           },
    //    	   success: function(response) {
    //    	        console.log("Success........");
    //    	   },
    //    	   error: handleAjaxError
    //    	});
    //}

    //UI DISPLAY METHODS

    function displayOrderList(data){
    	var $tbody = $('#brand-table').find('tbody');
    	$tbody.empty();
    	console.log("displayBrandList.............");
    	for(var i in data){
    		var e = data[i];
    		var row = '<tr>'
    		+ '<td>' + e.date + '</td>'
    		+ '<td>' + e.brand + '</td>'
    		+ '<td>'  + e.category + '</td>'
    		+ '<td>' + e.quantity + '</td>'
    		+ '<td>'  + e.revenue + '</td>'
    		+ '</tr>';
            $tbody.append(row);
    	}
    }

    //INITIALIZATION CODE
    function init(){
    	$('#refresh-data').click(getOrderList);
    //	$('#upload-data').click(getOrderListByFilter);
    }

    $(document).ready(init);
    $(document).ready(getOrderList);

//////////////////////////////////////
package com.increff.employee.controller;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class salesReportApiController {

	@Autowired
	private OrderItemService service;
	@Autowired
	private OrderService orderService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private BrandDao brandDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ProductDao productDao;
	LocalDate startDate,endDate;

	int findBrandCategory(List<SalesReportData> list,SalesReportData s){
		for(SalesReportData e: list){
			System.out.println("List Item:");
			String a,b,c,d;
			a=e.getBrand();
			b=s.getBrand();
			c=e.getCategory();
			d=s.getCategory();
			System.out.println(e.getId() + " " +a+ " " +c);
			System.out.println(s.getId() + " " +b+ " " +d);
			if(a.equals(b) && c.equals(d)){
				System.out.println("found");
				System.out.println(e.getId());
				return list.indexOf(e);
			}
		}return -1;
	}
	@ApiOperation(value = "Gets list of all orders")
	@RequestMapping(path = "/api/report/salesReport", method = RequestMethod.GET)
	public List<SalesReportData> getAll() {
		System.out.println("Hey there!!!!!!!!!!!!!!!!!!!");
		List<OrderItemPojo> orderList = service.getAll();
		List<SalesReportData> list = new ArrayList<SalesReportData>();
		for (OrderItemPojo p : orderList) {
			SalesReportData s= convert(p);
			int i=findBrandCategory(list,s);
			if(i==-1) {
				list.add(convert(p));
			}
			else{
				list.set(i,convert3(list.get(i),p));
			}
		}
		return list;
	}
	@ApiOperation(value = "Gets list of all orders")
	@RequestMapping(path = "/api/report/salesReport", method = RequestMethod.GET)
	public List<SalesReportData> getReleventAll(LocalDate startDate,LocalDate endDate) {
		System.out.println("GetRelevent in api!!!!!!!!!!!!!!!!!!!");
//		List<OrderPojo> list1 = orderService.getOrderDates(startDate,endDate);
//		List<OrderItemPojo> orderList= service.getRelevantAll(list1);
		List<SalesReportData> list = new ArrayList<SalesReportData>();
//		for (OrderItemPojo p : orderList) {
//			SalesReportData s= convert(p);
//			int i=findBrandCategory(list,s);
//			if(i==-1) {
//				list.add(convert(p));
//			}
//			else{
//				list.set(i,convert3(list.get(i),p));
//			}
//		}
		return list;
	}
//	@ApiOperation(value = "Applies a filter")
//	@RequestMapping(path = "/api/report/salesReport", method = RequestMethod.POST)
//	public void find(@RequestBody SalesReportData form) throws ApiException {
//		System.out.println("Post request successful!!!!!!");
//		startDate= form.getStartDate();
//		endDate= form.getEndDate();
//		System.out.println("startDate= "+startDate+" + endDate= " + endDate);
////		getReleventAll(startDate,endDate);
//	}

	private SalesReportData convert(OrderItemPojo p) {
		SalesReportData d = new SalesReportData();
		int brand_category=productDao.select(p.getProductId()).getBrand_category();
		d.setId(p.getId());
		d.setDate((orderDao.select(p.getOrderId()).getDateTime()).toLocalDate());
		d.setBrand(brandDao.select(brand_category).getBrand());
		d.setCategory(brandDao.select(brand_category).getCategory());
		d.setQuantity(p.getQuantity());
		d.setRevenue(p.getSellingPrice()*p.getQuantity());
		return d;
	}
	private SalesReportData convert3(SalesReportData d,OrderItemPojo p) {
		d.setQuantity(d.getQuantity()+p.getQuantity());
		d.setRevenue(d.getRevenue()+(p.getSellingPrice()*p.getQuantity()));
		return d;
	}
}
/////////////////////////
// Get the form element
const form = document.querySelector('form');

// Create an error message element
const errorContainer = document.createElement('div');
errorContainer.classList.add('error-message');
form.appendChild(errorContainer);

// Listen for the form submission event
form.addEventListener('submit', (event) => {
  event.preventDefault(); // Prevent the form from being submitted

  // Get the input value
  const inputValue = document.querySelector('#inputField').value;

  // Validate the input
  if (inputValue === '') {
    showError('Input cannot be empty'); // Call the showError function with the error message
    return; // Exit the function to prevent further processing
  }

  // If the input is valid, continue with form submission or further processing
  // ...
});

// Function to show the error message
function showError(message) {
  errorContainer.textContent = message; // Update the error message content
  errorContainer.style.display = 'block'; // Show the error message element
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////

function updateOrderItem(event){
	$('#edit-orderItem-modal').modal('toggle');
	//Get the ID
	var id = $("#orderItem-edit-form input[name=id]").val();
	var orderId=$("#orderItem-edit-form input[name=orderId]").val();
	var url = getOrderItemUrl() + "/" + id;

	//Set the values to update
	var $form = $("#orderItem-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
           console.log("updated");
           console.log("id=",id);
	   		viewOrder(orderId);
	   },
	   error: handleAjaxError
	});

	return false;
}

function viewOrderList(data){
	var $tbody = $('#view-order-table').find('tbody');
    	$tbody.empty();
    	var totalSellingPrice=0;
	for(var i in data){
		var e = data[i];
		console.log(e);
		console.log("e.id=");
		console.log(e.id);
		var buttonHtml1 = '<button type="button" class="btn btn-secondary" onclick="displayEditOrderItem(' + e.id + ')" >Edit</button>'
        var buttonHtml2 = '<button type="button" class="btn btn-secondary" (' + e.id + ')" >Delete</button>'
        var rowid= "row-"+e.id;
        console.log("viewOrderList rowid= ",rowid);
        var row = '<tr id='+rowid+'>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.productName + '</td>'
		+ '<td><input type="number" class="form-control" name="productId" placeholder="enter product Id" value="' + e.quantity + '"></td>'
		+ '<td><input type="number" inputmode="decimal" class="form-control" name="sellingPrice" placeholder="enter sellingPrice" value="' + e.sellingPrice + '"></td>'
		+ '<td>' + e.sellingPrice * e.quantity+ '</td>'
		+ '<td>' + buttonHtml1 + '</td>'
		+ '<td>' + buttonHtml2 + '</td>'
		+ '</tr>';
        $tbody.append(row);
        console.log("row is",row);
        totalSellingPrice+=(e.sellingPrice * e.quantity);
	}
	var $sellingPrice = $('#modal-footer').find('h6');
	$sellingPrice.text('Total Selling Price : '+ totalSellingPrice);
}

