package com.increff.pos.controller;

import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.ConversionUtil.convert;

@Api
@RestController
@RequestMapping(path = "/api/admin/user")
public class AdminController {
	@Autowired
	private UserService service;

	@ApiOperation(value = "Add a user")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public void addUser(@RequestBody UserForm form) throws ApiException {
		UserPojo pojo = convert(form);
		service.add(pojo);
	}

	@ApiOperation(value = "Delete a user")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable int id) throws ApiException{
		service.delete(id);
	}

	@ApiOperation(value = "Get list of all users")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<UserData> getAllUser() {
		List<UserPojo> list = service.getAll();
		List<UserData> list2 = new ArrayList<UserData>();
		for (UserPojo pojo : list) {
			list2.add(convert(pojo));
		}
		return list2;
	}
}
