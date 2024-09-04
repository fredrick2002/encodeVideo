package com.example.encodeVideo.controller;

import com.example.encodeVideo.model.Users;
import com.example.encodeVideo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

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
    public String login(@RequestBody Users user, HttpServletResponse response){
        String verfiyUser = service.verify(user);

        if(verfiyUser != null){
            Cookie cookie = new Cookie("Bearer", verfiyUser);
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            cookie.setSecure(false);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return "Cookie Set " + cookie;
        }
        return "fail";
    }
}
