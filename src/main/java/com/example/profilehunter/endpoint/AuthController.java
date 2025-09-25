package com.example.profilehunter.endpoint;

import com.example.profilehunter.model.dto.auth.AuthRequest;
import com.example.profilehunter.model.dto.auth.AuthResponse;
import com.example.profilehunter.model.dto.UserDto;
import com.example.profilehunter.service.auth.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", methods = { RequestMethod.GET, RequestMethod.POST })
public class AuthController {

    private final IAuthService iAuthService;

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return iAuthService.loginUser(authRequest);
    }

    @PostMapping("/register")
    public Mono<UserDto> register(UserDto userDto) {
        return iAuthService.registerUser(userDto);
    }

    @GetMapping("/token/valid")
    public Mono<Boolean> validateToken(@RequestParam String token) {
        return iAuthService.validateToken(token);
    }

}
