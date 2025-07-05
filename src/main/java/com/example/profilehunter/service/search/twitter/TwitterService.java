package com.example.profilehunter.service.search.twitter;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.mapper.UserMapperFactory;
import com.example.profilehunter.service.search.BaseSearchService;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Get2UsersByResponse;
import com.twitter.clientlib.model.Get2UsersByUsernameUsernameResponse;
import com.twitter.clientlib.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
public class TwitterService extends BaseSearchService<User, User> {

    private final TwitterApi twitterApi;

    public TwitterService(TwitterApi twitterApi, UserMapperFactory mapperFactory) {
        super(mapperFactory);
        this.twitterApi = twitterApi;
    }

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
    public SourceType getSourceType() {
        return SourceType.X;
    }
}
