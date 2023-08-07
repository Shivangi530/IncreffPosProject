package com.increff.pos.controller;

import com.increff.pos.dto.brandDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/brand")
public class BrandController {
	@Autowired
	private brandDto brandDto;

	@ApiOperation(value = "Add a brand")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		brandDto.add(form);
	}

	@ApiOperation(value = "Get a brand by ID")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public BrandData getBrand(@PathVariable Integer id) throws ApiException {
		return brandDto.getBrand(id);
	}

	@ApiOperation(value = "Get list of all brands")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<BrandData> getAll() {
		return brandDto.getAll();
	}

	@ApiOperation(value = "Update a brand")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer id, @RequestBody BrandForm form) throws ApiException {
		brandDto.update(id, form);
	}

	// For tsv file upload
	@ApiOperation(value = "Add list of brands")
	@RequestMapping(path = "/list", method = RequestMethod.POST)
	public void addList(@RequestBody List<BrandForm> brandFormList) throws ApiException {
		brandDto.addList(brandFormList);
	}
}

