package com.increff.employee.controller;

import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.service.ApiException;
import com.increff.employee.dto.orderItemDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@RestController
public class OrderItemApiController {

	@Autowired
	private orderItemDto dto;

	@ApiOperation(value = "Add an orderItem")
	@RequestMapping(path = "/api/orderItem", method = RequestMethod.POST)
	public void add(@RequestBody OrderItemForm form) throws ApiException {
		dto.add(form);
	}

	@ApiOperation(value = "Delete an orderItem")
	@RequestMapping(path = "/api/orderItem/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		dto.delete(id);
	}

	@ApiOperation(value = "Get an orderItem by ID")
	@RequestMapping(path = "/api/orderItem/{id}", method = RequestMethod.GET)
	public OrderItemData get(@PathVariable int id) throws ApiException {
		return dto.get(id);
	}
	@ApiOperation(value = "Get the list of orderItem by orderID")
	@RequestMapping(path = "/api/orderItem/view/{id}", method = RequestMethod.GET)
	public List<OrderItemData> getOrderList(@PathVariable int id) throws ApiException {
		return dto.getOrderList(id);
	}

	@ApiOperation(value = "Get list of all orderItems")
	@RequestMapping(path = "/api/orderItem", method = RequestMethod.GET)
	public List<OrderItemData> getAll() {
		return dto.getAll();
	}

	@ApiOperation(value = "Updates an orderItem")
	@RequestMapping(path = "/api/orderItem/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
		dto.update(id,f);
	}
}
