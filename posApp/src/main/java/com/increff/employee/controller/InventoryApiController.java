package com.increff.employee.controller;

import com.increff.employee.dto.inventoryDto;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@RestController
public class InventoryApiController {

	@Autowired
	private inventoryDto inventoryDto;

	@ApiOperation(value = "Get an inventory by ID")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException{
		return inventoryDto.get(id);
	}

	@ApiOperation(value = "Get list of all inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException{
		return inventoryDto.getAll();
	}

	@ApiOperation(value = "Update an inventory")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
		inventoryDto.update(id,f);
	}

}
