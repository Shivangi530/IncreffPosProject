package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import static junit.framework.TestCase.assertEquals;

public class loginDtoTest extends AbstractUnitTest {

    @Autowired
    private loginDto dto;
    @Autowired
    private UserService service;
    @Autowired
    private InfoData info;

    @Test
    public void testSignUp() throws ApiException {

        LoginForm loginForm= new LoginForm();
        loginForm.setEmail("test@example.com");
        loginForm.setPassword("Test@123");

        ModelAndView modelAndView =dto.signUp(loginForm);

        assertEquals("redirect:/site/login", modelAndView.getViewName());
        assertEquals("User registered successfully", info.getMessage());
        assertEquals(EnumData.Role.OPERATOR.toString(), info.getRole());

        UserPojo user= service.get("test@example.com");
        assertEquals(loginForm.getPassword(),user.getPassword());
        assertEquals("OPERATOR",user.getRole().toString());
    }

    @Test
    public void testSignUp_UserExists() throws ApiException{

        UserPojo existingUser = new UserPojo();
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("Test@12343");
        existingUser.setRole(EnumData.Role.OPERATOR);
        service.add(existingUser);

        LoginForm loginForm= new LoginForm();
        loginForm.setEmail("test@example.com");
        loginForm.setPassword("Test@123");

        ModelAndView modelAndView =dto.signUp(loginForm);

        assertEquals("redirect:/site/login", modelAndView.getViewName());
        assertEquals("User already exists", info.getMessage());
    }

    @Test
    public void testSignUp_ApiException() throws ApiException {
        LoginForm loginForm= new LoginForm();
        loginForm.setEmail("test@example.com");
        loginForm.setPassword(""); // invalid password

        ModelAndView modelAndView =dto.signUp(loginForm);

        // Verify that the user registration failed and redirected to the login page
        assertEquals("redirect:/site/login", modelAndView.getViewName());
        assertEquals("Error occurred during user registration", info.getMessage());
    }

}
