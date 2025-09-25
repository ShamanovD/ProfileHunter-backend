package com.example.profilehunter.service.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Primary
public class JWTTokenAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String username = jwtService.getUserNameFromJwtToken(token);

        return userDetailsService.findByUsername(username).handle((user, sink) -> {
            if (jwtService.validateToken(token, user.getUsername())) {
                Authentication authenticatedToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                sink.next(authenticatedToken);
            } else {
                sink.error(new AuthenticationException("Invalid JWT token") {});
            }
        });
    }
}
