package com.seek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.seek.dto.LoginRequest;
import com.seek.dto.LoginResponse;
import com.seek.dto.UserRequest;
import com.seek.dto.UserResponse;
import com.seek.service.UserService;
import com.seek.util.JWTUtil;

public class UserController {

    @Autowired
    private UserService userService;

    private final JWTUtil jWTUtil = new JWTUtil();

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        try {

            LoginResponse loginResponse;
            loginResponse = this.userService.login(loginRequest);

            return ResponseEntity.ok(loginResponse);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Login error: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody UserRequest userRequest,
            @RequestHeader("Authorization") String jwt) {
        try {

            if (!this.jWTUtil.isTokenValid(jwt)) {
                return ResponseEntity
                        .badRequest()
                        .body("Invalid JWT");
            }

            UserResponse userResponse;
            userResponse = this.userService.create(userRequest);

            return ResponseEntity.ok(userResponse);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error creating user: " + e.getMessage());
        }
    }

}
