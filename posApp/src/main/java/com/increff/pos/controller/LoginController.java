package com.increff.pos.controller;

import com.increff.pos.dto.loginDto;
import com.increff.pos.model.LoginForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(path = "/session/")
public class LoginController {

    @Autowired
    private loginDto dto;

    @ApiOperation(value = "Register a new user")
    @RequestMapping(path = "signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView signUp(LoginForm form) throws ApiException{
        return dto.signUp(form);
    }

    @ApiOperation(value = "Log in a user")
    @RequestMapping(path = "login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView login(HttpServletRequest req, LoginForm form) throws ApiException {
        return dto.login(req,form);
    }

    @RequestMapping(path = "logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        return dto.logout(request,response);
    }

}
