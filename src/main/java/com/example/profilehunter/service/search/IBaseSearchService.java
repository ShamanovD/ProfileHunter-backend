package com.example.profilehunter.service.search;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.filter.LinkedUsersFilter;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface IBaseSearchService<T, R> {

    Mono<UserInfo> getUserByUsername(String username);

    Mono<List<UserInfo>> getUserByDeepSearch(String username, LinkedUsersFilter filter);

    Mono<UserFullInfo> getUserFullInfo(String username);

    Function<String, Optional<T>> searchUser();

    Function<String, Optional<R>> searchUserFullInfo();

    Function<String, List<T>> searchUsers();

    Function<T, UserInfo> mapUser();

    Function<R, UserFullInfo> mapUserWithFullInfo();

    SourceType getSourceType();

}
