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
    public void login(@RequestBody Users user, HttpServletResponse response){
        String verfiyUser = service.verify(user);

        if(verfiyUser != null){
            Cookie cookie = new Cookie("Bearer", verfiyUser);
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            // Set the response status to 200 OK
            response.setStatus(HttpServletResponse.SC_OK);

            // Write the response message
            try {
                response.getWriter().write("Login successful. Cookie Set.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Set the response status to 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // Write the response message
            try {
                response.getWriter().write("Invalid credentials");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
