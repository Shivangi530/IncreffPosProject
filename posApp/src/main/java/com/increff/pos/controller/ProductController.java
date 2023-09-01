package com.increff.pos.controller;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.ProductTsvForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@RestController
public class ProductController {

	@Autowired
	private com.increff.pos.dto.productDto productDto;

	@ApiOperation(value = "Add a product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm  form) throws ApiException {
		productDto.add(form);
	}

	@ApiOperation(value = "Get a product by ID")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable Integer id) throws ApiException {
		return productDto.get(id);
	}

	@ApiOperation(value = "Get list of all products")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll(){
		return productDto.getAll();
	}

	@ApiOperation(value = "Update a product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer id, @RequestBody ProductForm form) throws ApiException {
		productDto.update(id,form);
	}

	@ApiOperation(value = "Add list of product")
	@RequestMapping(path = "/api/product/list", method = RequestMethod.POST)
	public void addList(@RequestBody List<ProductTsvForm> productFormList) throws ApiException {
		productDto.addList(productFormList);
	}


}
