package com.seek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.seek.dao.UserRepository;
import com.seek.dto.LoginRequest;
import com.seek.dto.LoginResponse;
import com.seek.dto.UserRequest;
import com.seek.dto.UserResponse;
import com.seek.model.User;
import com.seek.util.JWTUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jWTUtil;

    public LoginResponse login(LoginRequest loginRequest)
            throws Exception {

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new Exception("Invalid credentials"));

        if (this.passwordEncoder.matches(loginRequest.getPassword(),
                user.getPassword())) {
            return new LoginResponse(this.jWTUtil
                    .generateToken(),
                    "Login successful");
        } else {
            throw new Exception("Invalid credentials");
        }
    }

    public UserResponse create(UserRequest userRequest)
            throws Exception {
        this.userRepository.findByUsername(userRequest.getUsername()).ifPresent(user -> {
            throw new RuntimeException("Username already registered");
        });

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(
                this.passwordEncoder.encode(userRequest.getPassword()));

        this.userRepository.save(user);

        return new UserResponse(
                user.getUsername(),
                "User created");
    }

}
