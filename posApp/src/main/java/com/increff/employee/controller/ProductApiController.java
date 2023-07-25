package com.increff.employee.controller;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@RestController
public class ProductApiController {

	@Autowired
	private com.increff.employee.dto.productDto productDto;

	@ApiOperation(value = "Add a product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm  form) throws ApiException {
		// TODO try putting a list of form
		productDto.add(form);
	}


	@ApiOperation(value = "Get a product by ID")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		return productDto.get(id);
	}

	@ApiOperation(value = "Get list of all products")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll(){
		return productDto.getAll();
	}

	@ApiOperation(value = "Update an product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {
		productDto.update(id,f);
	}

}
