package com.increff.pos.controller;

import com.increff.pos.dto.createOrderDto;
import com.increff.pos.model.CreateOrderForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@RestController
public class CreateOrderApiController {

	@Autowired
	private createOrderDto dto;

	@ApiOperation(value = "Check the order")
	@RequestMapping(path = "/api/createOrder", method = RequestMethod.POST)
	public void add(@RequestBody CreateOrderForm form) throws ApiException {
		dto.add(form);
	}

	@ApiOperation(value = "Get list of all orders")
	@RequestMapping(path = "/api/createOrder/all", method = RequestMethod.POST)
	public void checkAll(@RequestBody List<CreateOrderForm> formList) throws ApiException{
		System.out.println("checkAll called");
		dto.checkAll(formList);
	}
}
