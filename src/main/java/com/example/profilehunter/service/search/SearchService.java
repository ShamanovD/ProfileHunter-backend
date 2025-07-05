package com.example.profilehunter.service.search;

import com.example.profilehunter.model.common.SearchType;
import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.filter.LinkedUsersFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SearchService implements ISearchService {

    private final SearchServiceFactory searchServiceFactory;

    @Override
    @Cacheable("SearchService_getStartUserNodeByUsername")
    public Mono<UserInfo> getStartUserNodeByUsername(String username, SourceType type) {
        IBaseSearchService<?, ?> searchService = searchServiceFactory.getSearchServiceBySearchType(type);

        if (Objects.nonNull(searchService)) {
            return searchService.getUserByUsername(username);
        }

        return Mono.empty();
    }

    @Override
    public Flux<UserInfo> getLinkedUsers(String username, LinkedUsersFilter filter) {
        List<IBaseSearchService<?, ?>> services = searchServiceFactory.getServicesFilteredByStartNodeSearchType(filter.getStartNodeType());

        services = services.stream().filter(item -> filter.getSourceTypes().contains(item.getSourceType())).toList();

        return Flux.fromIterable(services).flatMap(item -> {
            if (SearchType.QUICK.equals(filter.getSearchType())) {
                return item.getUserByUsername(username);
            } else if (SearchType.DEEP.equals(filter.getSearchType())) {
                return item.getUserByDeepSearch(username, filter)
                        .flatMapMany(Flux::fromIterable);
            }

            return Flux.empty();
        }).filter(Objects::nonNull);
    }

    @Override
    public Mono<UserFullInfo> getUserFullInfo(String username, SourceType type) {
        IBaseSearchService<?, ?> searchService = searchServiceFactory.getSearchServiceBySearchType(type);

        if (Objects.nonNull(searchService)) {
            return searchService.getUserFullInfo(username);
        }

        return Mono.empty();
    }
}
