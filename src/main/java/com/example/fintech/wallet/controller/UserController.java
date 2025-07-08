package com.example.fintech.wallet.controller;

import com.example.fintech.wallet.dto.UserResponseDTO;
import com.example.fintech.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users/me")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public UserResponseDTO getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserByUsername(userDetails.getUsername());
    }
} 