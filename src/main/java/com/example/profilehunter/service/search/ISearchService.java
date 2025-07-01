package com.example.profilehunter.service.search;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.filter.LinkedUsersFilter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISearchService {

    Mono<UserInfo> getStartUserNodeByUsername(String username, SourceType type);

    Flux<UserInfo> getLinkedUsers(String username, LinkedUsersFilter filter);

    Mono<UserFullInfo> getUserFullInfo(String username, SourceType type);
}
