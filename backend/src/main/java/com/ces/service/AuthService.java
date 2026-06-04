package com.ces.service;

import com.ces.dto.AuthDto;

public interface AuthService {
    AuthDto.JwtResponse login(AuthDto.LoginRequest request);
    AuthDto.MessageResponse register(AuthDto.RegisterRequest request);
}
