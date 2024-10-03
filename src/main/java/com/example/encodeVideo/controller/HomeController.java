package com.example.encodeVideo.controller;

import com.example.encodeVideo.model.Users;
import com.example.encodeVideo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class HomeController {

    @Autowired
    private UserService service;


    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken)request.getAttribute("_csrf") ;
    }

    @PostMapping("/register")
    public Users register(@RequestBody Users user){
        return service.register(user);
    }

    @PostMapping("/login")
    public void login(@RequestBody Users user, HttpServletResponse response) {
        String verifyUser = service.verify(user);

        if (verifyUser != null) {
            // Set the cookie with the JWT or token
            Cookie cookie = new Cookie("Bearer", verifyUser);
            cookie.setMaxAge(3600);  // Expires in 1 hour
            cookie.setPath("/");
            cookie.setSecure(false);// Only allow over HTTPS
            cookie.setDomain("localhost");
            cookie.setHttpOnly(true);  // Prevent access via JavaScript
            response.addCookie(cookie);

            // Set the response status to 200 OK
            response.setStatus(HttpServletResponse.SC_OK);

            // Write the response message as a JSON object
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                response.getWriter().write("{\"message\": \"Login successful. Cookie Set.\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // Set the response status to 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // Write the response message as a JSON object
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                response.getWriter().write("{\"message\": \"Invalid credentials\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
