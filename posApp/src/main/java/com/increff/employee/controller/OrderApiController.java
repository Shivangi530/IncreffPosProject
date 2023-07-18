package com.increff.employee.controller;

import com.increff.employee.model.OrderData;
import com.increff.employee.dto.orderDto;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
	
	@ApiOperation(value = "Delete an order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		dto.delete(id);
	}

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
	public void update(@PathVariable int id ) throws ApiException {
		dto.update(id);
	}

	@ApiOperation(value = "Download Invoice")
	@GetMapping(path = "/api/invoice/{id}")
	public ResponseEntity<byte[]> getInvoicePDF(@PathVariable Integer id) throws Exception{
		return dto.getInvoicePDF(id);
	}

}
