package com.increff.pos.controller;

import com.increff.pos.dto.inventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@RestController
public class InventoryController {

	@Autowired
	private inventoryDto inventoryDto;

	@ApiOperation(value = "Get an inventory by ID")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable Integer id) throws ApiException{
		return inventoryDto.get(id);
	}

	@ApiOperation(value = "Get list of all inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException{
		return inventoryDto.getAll();
	}

	@ApiOperation(value = "Update an inventory")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer id, @RequestBody InventoryForm form) throws ApiException {
		inventoryDto.update(id,form);
	}

	@ApiOperation(value = "Update multiple inventory")
	@RequestMapping(path = "/api/inventory/list", method = RequestMethod.PUT)
	public void updateList(@RequestBody List<InventoryForm> inventoryFormList) throws ApiException {
		inventoryDto.updateList(inventoryFormList);
	}

}
