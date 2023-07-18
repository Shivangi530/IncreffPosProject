package com.increff.employee.controller;

import com.increff.employee.model.InfoData;
import com.increff.employee.model.LoginForm;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.UserService;
import com.increff.employee.util.SecurityUtil;
import com.increff.employee.util.UserPrincipal;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

@Controller
public class LoginController {

    @Autowired
    private UserService service;
    @Autowired
    private InfoData info;
    @Value("${roles.path}")
    private String rolesPath;

    @ApiOperation(value = "Register a new user")
    @RequestMapping(path = "/session/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView signUp(LoginForm f) throws ApiException{
        info.setMessage("");
        try {
            System.out.println("email "+f.getEmail());
            System.out.println("password "+f.getPassword());
            // Check if the user already exists
            UserPojo existingUser = service.get(f.getEmail());
            if (existingUser != null) {
                info.setMessage("User already exists");
                return new ModelAndView("redirect:/site/login");
            }

            // Create a new user
            UserPojo newUser = new UserPojo();
            newUser.setEmail(f.getEmail());
            newUser.setPassword(f.getPassword());

            // Assign role based on email
            String role = getRoleFromProperties(newUser.getEmail());
            newUser.setRole(role);

            // Set other user properties as required

            // Save the new user
            service.add(newUser);

            // Redirect to the login page
            info.setMessage("User registered successfully");
            return new ModelAndView("redirect:/site/login");
        } catch (ApiException e) {
            // Handle any exceptions that occur during sign up
            info.setMessage("Error occurred during user registration");
            return new ModelAndView("redirect:/site/login");
        }
    }

    private String getRoleFromProperties(String email) {
        // Load properties from file
        Properties properties = new Properties();
        try {
            FileInputStream file = new FileInputStream(rolesPath);
            properties.load(file);
            file.close();
        } catch (IOException e) {
            // Handle file loading error
            e.printStackTrace();
            return null; // Or throw an exception
        }

        // Check if the email exists in properties
        String role = properties.getProperty(email);
        if (role != null && !role.isEmpty()) {
            // Email found, assign supervisor role
            return "supervisor";
        } else {
            // Email not found, assign operator role
            return "operator";
        }
    }


    @ApiOperation(value = "Log in a user")
    @RequestMapping(path = "/session/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView login(HttpServletRequest req, LoginForm f) throws ApiException {
        info.setMessage("");
        UserPojo p = service.get(f.getEmail());
        boolean authenticated = (p != null && Objects.equals(p.getPassword(), f.getPassword()));
        if (!authenticated) {
            info.setMessage("Invalid username or password");
            return new ModelAndView("redirect:/site/login");
        }

        // Create authentication object
        Authentication authentication = convert(p);
        // Create new session
        HttpSession session = req.getSession(true);
        // Attach Spring SecurityContext to this new session
        SecurityUtil.createContext(session);
        // Attach Authentication object to the Security Context
        SecurityUtil.setAuthentication(authentication);

        return new ModelAndView("redirect:/ui/home");

    }

    @RequestMapping(path = "/session/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return new ModelAndView("redirect:/site/logout");
    }

    private static Authentication convert(UserPojo p) {
        // Create principal
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(p.getEmail());
        principal.setId(p.getId());

        // Create Authorities
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(p.getRole()));
        // you can add more roles if required

        // Create Authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
                authorities);
        return token;
    }

}
