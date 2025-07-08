package com.example.fintech.wallet.service;

import com.example.fintech.wallet.dto.UserRegisterDTO;
import com.example.fintech.wallet.dto.UserLoginDTO;
import com.example.fintech.wallet.dto.UserResponseDTO;
import com.example.fintech.wallet.dto.AuthResponseDTO;

public interface UserService {

    
    UserResponseDTO registerUser(UserRegisterDTO registerDTO);

   
    AuthResponseDTO login(UserLoginDTO loginDTO);

    UserResponseDTO getUserByUsername(String username);
} 