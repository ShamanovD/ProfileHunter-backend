package com.example.profilehunter.service.auth.service;

import com.example.profilehunter.model.database.User;
import com.example.profilehunter.model.dto.auth.AuthRequest;
import com.example.profilehunter.model.dto.auth.AuthResponse;
import com.example.profilehunter.model.dto.UserDto;
import com.example.profilehunter.model.mapper.auth.IUserMapper;
import com.example.profilehunter.service.auth.jwt.JwtService;
import com.example.profilehunter.service.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final IUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ReactiveAuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, IUserMapper userMapper, PasswordEncoder passwordEncoder,
                       JwtService jwtService, @Qualifier("usernamePasswordAuthManager") ReactiveAuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<AuthResponse> loginUser(AuthRequest authRequest) {
        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()))
                .flatMap(authentication -> {
                    String token = jwtService.generateJwtToken(authentication);

                    Date expirationDate = jwtService.getExpirationFromJwtToken(token);

                    return Mono.just(new AuthResponse(token, expirationDate.getTime()));
                });
    }

    @Override
    public Mono<UserDto> registerUser(UserDto userDto) {
        return Mono.fromCallable(() -> {
                    if (!userRepository.existsByUsernameOrEmailOrPhone(userDto.getUsername(), userDto.getEmail(), userDto.getPhone())) {
                        User user = userMapper.toUser(userDto);

                        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

                        return userRepository.save(user);
                    }

                    throw new RuntimeException("User already exist");
                })
                .map(userMapper::toDto)
                .onErrorResume(RuntimeException.class,
                        e -> Mono.error(new RuntimeException("Failed to register user.", e)));
    }

    @Override
    public Mono<User> getUser(String username) {
        return Mono.fromCallable(() ->
                userRepository.findByUsernameOrEmailOrPhone(username, username, username)
                        .orElseThrow());
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> jwtService.isTokenExpired(token));
    }

}
