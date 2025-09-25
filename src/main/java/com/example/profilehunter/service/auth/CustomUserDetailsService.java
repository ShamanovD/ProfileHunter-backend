package com.example.profilehunter.service.auth;

import com.example.profilehunter.model.auth.CustomUserDetails;
import com.example.profilehunter.model.database.User;
import com.example.profilehunter.service.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromCallable(() ->
                userRepository.findByUsernameOrEmailOrPhone(username, username, username)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with username: " + username)))
                .map(CustomUserDetails::new);
    }
}
