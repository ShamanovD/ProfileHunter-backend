package com.example.profilehunter.service.search.twitter;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.mapper.twitter.TwitterInfoMapper;
import com.example.profilehunter.service.search.BaseSearchService;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Get2UsersByResponse;
import com.twitter.clientlib.model.Get2UsersByUsernameUsernameResponse;
import com.twitter.clientlib.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TwitterService extends BaseSearchService<User, User> {

    private final TwitterApi twitterApi;
    private final TwitterInfoMapper twitterInfoMapper;

    @Override
    public Function<String, Optional<User>> searchUser() {
        return item -> {
            try {
                Get2UsersByUsernameUsernameResponse users = twitterApi.users().findUserByUsername(item).execute();

                if (Objects.isNull(users.getErrors())) {
                    return Optional.ofNullable(users.getData());
                }
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
            return Optional.empty();
        };
    }

    @Override
    public Function<String, Optional<User>> searchUserFullInfo() {
        return searchUser();
    }

    @Override
    public Function<String, List<User>> searchUsers() {
        return item -> {
            try {
                Get2UsersByResponse users = twitterApi.users().findUsersByUsername(List.of(item)).execute();

                if (Objects.isNull(users.getErrors())) {
                    return users.getData();
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }

            return new ArrayList<>();
        };
    }

    @Override
    public Function<User, UserInfo> mapUser() {
        return twitterInfoMapper::mapUser;
    }

    @Override
    public Function<User, UserFullInfo> mapUserWithFullInfo() {
        return twitterInfoMapper::mapUserWithFullInfo;
    }

    @Override
    public SourceType getSearchType() {
        return SourceType.TWITTER;
    }
}
