package com.example.profilehunter.service.search;

import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.filter.LinkedUsersFilter;
import com.example.profilehunter.model.mapper.IUserFullInfoMapper;
import com.example.profilehunter.model.mapper.UserMapperFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class BaseSearchService<T, R> implements IBaseSearchService<T, R> {

    private final IUserFullInfoMapper<T, R> userFullInfoMapper;

    protected BaseSearchService(UserMapperFactory mapperFactory) {
        userFullInfoMapper = (IUserFullInfoMapper<T, R>) mapperFactory.getUserMapperBySourceType(getSourceType());
    }

    @Override
    public Mono<UserInfo> getUserByUsername(String username) {
        return Mono.fromCallable(() -> {
                    Optional<T> userOpt = searchUser().apply(username);

                    return userOpt.map(t -> mapUser().apply(t));
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(userInfo -> userInfo.map(Mono::just).orElseGet(Mono::empty));
    }

    @Override
    public Mono<List<UserInfo>> getUserByDeepSearch(String username, LinkedUsersFilter filter) {
        return Mono.fromCallable(() -> {
                    List<T> users = searchUsers().apply(username);

                    return users.stream().map(t -> mapUser().apply(t)).toList();
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UserFullInfo> getUserFullInfo(String username) {
        return Mono.fromCallable(() -> {
                    Optional<R> userOpt = searchUserFullInfo().apply(username);

                    return userOpt.map(r -> mapUserWithFullInfo().apply(r));
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(userInfo -> userInfo.map(Mono::just).orElseGet(Mono::empty));
    }

    @Override
    public Function<T, UserInfo> mapUser() {
        return userFullInfoMapper::mapUser;
    }

    @Override
    public Function<R, UserFullInfo> mapUserWithFullInfo() {
        return userFullInfoMapper::mapUserWithFullInfo;
    }
}
