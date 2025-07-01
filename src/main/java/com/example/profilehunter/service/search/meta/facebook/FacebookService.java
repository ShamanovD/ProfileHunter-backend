package com.example.profilehunter.service.search.meta.facebook;

import com.example.profilehunter.model.dto.UserInfo;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacebookService implements IFacebookService {

    private final FacebookClient client;

    @Override
    public UserInfo getUserInfoByUsername(String username) {
        try {
            User user = client.fetchObject("me", User.class);
            System.out.println("Имя: " + user.getName());
            System.out.println("ID: " + user.getId());
        } catch (com.restfb.exception.FacebookException e) {
            e.printStackTrace();
        }

        return null;
    }
}
