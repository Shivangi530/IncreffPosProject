package com.increff.pos.controller;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.increff.pos.util.ConversionUtil.convert;

@Controller
public class InitController extends AbstractUiController {

	@Autowired
	private UserService service;
	@Autowired
	private InfoData info;

	@ApiOperation(value = "Initialize application")
	@RequestMapping(path = "/site/init", method = RequestMethod.GET)
	public ModelAndView showPage(UserForm form) throws ApiException {
		info.setMessage("");
		return mav("init.html");
	}

	@ApiOperation(value = "Initialize application")
	@RequestMapping(path = "/site/init", method = RequestMethod.POST)
	public ModelAndView initSite(UserForm form) throws ApiException {
		List<UserPojo> list = service.getAll();
		if (list.size() > 0) {
			info.setMessage("Application already initialized. Please use existing credentials");
		} else {
			form.setRole("ADMIN");
			UserPojo p = convert(form);
			service.add(p);
			info.setMessage("Application initialized");
		}
		return mav("init.html");
	}

}
