package com.example.profilehunter.service.auth.service;

import com.example.profilehunter.model.database.User;
import com.example.profilehunter.model.dto.auth.AuthRequest;
import com.example.profilehunter.model.dto.auth.AuthResponse;
import com.example.profilehunter.model.dto.UserDto;
import reactor.core.publisher.Mono;

public interface IAuthService {

    Mono<AuthResponse> loginUser(AuthRequest authRequest);

    Mono<UserDto> registerUser(UserDto userDto);

    Mono<User> getUser(String username);

    Mono<Boolean> validateToken(String token);
}
