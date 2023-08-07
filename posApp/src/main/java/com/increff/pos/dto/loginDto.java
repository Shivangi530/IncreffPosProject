package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

@Component
public class loginDto {
    @Autowired
    private UserService service;
    @Autowired
    private InfoData info;
    @Value("${roles.path}")
    private String rolesPath;

    public ModelAndView signUp(LoginForm f) {
        info.setMessage("");
        try {
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
            String roleValue = getRoleFromProperties(newUser.getEmail());
            EnumData.Role role = EnumData.Role.valueOf(roleValue);
            newUser.setRole(role);

            // Save the new user
            service.add(newUser);

            // Redirect to the login page
            info.setMessage("User registered successfully");
            info.setRole(String.valueOf(role));
            return new ModelAndView("redirect:/site/login");
        } catch (ApiException e) {
            // Handle any exceptions that occur during sign up
            info.setMessage("Error occurred during user registration");
            return new ModelAndView("redirect:/site/login");
        }
    }

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

        session.setAttribute("role",String.valueOf(p.getRole()));
        info.setRole(String.valueOf(p.getRole()));
        return new ModelAndView("redirect:/ui/home");
    }

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
        EnumData.Role role = p.getRole();
        String roleString = role.name();
        authorities.add(new SimpleGrantedAuthority(roleString));

        // Create Authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
                authorities);
        return token;
    }

    private String getRoleFromProperties(String email) {
        // Load properties from file
        Properties properties = new Properties();
        try {
            FileInputStream file = new FileInputStream(rolesPath);
            properties.load(file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Check if the email exists in properties
        String role = properties.getProperty(email);
        if (role != null && !role.isEmpty()) {
            return "SUPERVISOR";
        } else {
            return "OPERATOR";
        }
    }
}