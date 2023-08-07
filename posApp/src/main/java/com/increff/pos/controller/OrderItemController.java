package com.increff.pos.controller;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.dto.orderItemDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/order-item")
public class OrderItemController {

	@Autowired
	private orderItemDto dto;

	@ApiOperation(value = "Delete an orderItem")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) throws ApiException {
		dto.delete(id);
	}

	@ApiOperation(value = "Get the list of orderItem by orderID")
	@RequestMapping(path = "/view/{id}", method = RequestMethod.GET)
	public List<OrderItemData> getOrderList(@PathVariable int id) throws ApiException {
		return dto.getOrderList(id);
	}

	@ApiOperation(value = "Updates an orderItem")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderItemForm form) throws ApiException {
		dto.update(id,form);
	}
}
