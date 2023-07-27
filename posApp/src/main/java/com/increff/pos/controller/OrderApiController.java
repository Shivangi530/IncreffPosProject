package com.increff.pos.controller;

import com.increff.pos.model.OrderData;
import com.increff.pos.dto.orderDto;
import com.increff.pos.model.OrderForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class OrderApiController {

	@Autowired
	private orderDto dto;
	@ApiOperation(value = "Add an order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public int add() throws ApiException {
		return dto.add();
	}
	
//	@ApiOperation(value = "Delete an order")
//	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
//	public void delete(@PathVariable int id) {
//		dto.delete(id);
//	}

	@ApiOperation(value = "Get an order by ID")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
	public OrderData get(@PathVariable int id) throws ApiException {
		return dto.get(id);
	}

	@ApiOperation(value = "Get list of all orders")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<OrderData> getAll() {
		return dto.getAll();
	}

	@ApiOperation(value = "Update an order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody String status) throws ApiException {
		dto.update(id,status);
	}

	@ApiOperation(value = "Download Invoice")
	@GetMapping(path = "/api/invoice/{id}")
	public ResponseEntity<byte[]> getInvoicePDF(@PathVariable Integer id) throws Exception{
		return dto.getInvoicePDF(id);
	}

}
