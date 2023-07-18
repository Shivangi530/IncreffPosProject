package com.increff.employee.controller;

import com.increff.employee.dto.brandDto;
import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@RestController
public class BrandApiController {

	@Autowired
	private brandDto brandDto;

	@ApiOperation(value = "Add a brand")
	@RequestMapping(path = "/api/brand", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		brandDto.add(form);
	}

	@ApiOperation(value = "Get a brand by ID")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
	public BrandData getBrand(@PathVariable int id) throws ApiException {
		return brandDto.getBrand(id);
	}

	@ApiOperation(value = "Get list of all brands")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public List<BrandData> getAll() {
		return brandDto.getAll();
	}

	@ApiOperation(value = "Update a brand")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
		brandDto.update(id, f);
	}
}

