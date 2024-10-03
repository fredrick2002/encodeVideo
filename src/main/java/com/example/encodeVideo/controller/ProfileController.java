package com.example.encodeVideo.controller;

import com.example.encodeVideo.model.UserVideo;
import com.example.encodeVideo.model.Users;
import com.example.encodeVideo.repository.VideoRepository;
import com.example.encodeVideo.service.JWTService;
import com.example.encodeVideo.service.UserService;
import com.example.encodeVideo.service.VideoDBService;
import com.example.encodeVideo.utils.AuthenticationUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.COOKIE;

@RestController
@RequestMapping
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private VideoDBService videoDBService;


    String username;

    @GetMapping("/home")
    public ResponseEntity<?> getUserProfile(@RequestHeader(COOKIE)String authHeader) {
        String token = authHeader.substring(7);
        username = jwtService.extractUserName(token);
//        System.out.println(username);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Fetch user by email from the database
        Users user = userService.getUserByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Return user profile as response
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getvideo")
    public UserVideo getUserVideos(){
        return videoDBService.getVideo(AuthenticationUtil.getCurrentUsername());
    }
}
