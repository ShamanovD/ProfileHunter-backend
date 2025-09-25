package com.example.profilehunter.model.auth;

import com.example.profilehunter.model.database.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        if (Objects.nonNull(user.getUsername())) {
            return user.getUsername();
        }
        if (Objects.nonNull(user.getEmail())) {
            return user.getEmail();
        }
        if (Objects.nonNull(user.getPhone())) {
            return user.getPhone();
        }

        throw new RuntimeException("User have not login fields");
    }

    public User getUser() {
        return this.user;
    }
}
