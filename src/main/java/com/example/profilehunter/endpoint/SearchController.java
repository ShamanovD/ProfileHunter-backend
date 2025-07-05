package com.example.profilehunter.endpoint;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.filter.LinkedUsersFilter;
import com.example.profilehunter.service.search.ISearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("search")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", methods = { RequestMethod.GET, RequestMethod.POST })
public class SearchController {

    private final ISearchService searchService;

    @GetMapping("start")
    public Mono<UserInfo> getStartUserInfo(@RequestParam String username, @RequestParam SourceType type) {
        return searchService.getStartUserNodeByUsername(username, type);
    }

    @PostMapping("linked")
    public Flux<UserInfo> getLinkedUsers(@RequestParam String username, @RequestBody LinkedUsersFilter filter) {
        return searchService.getLinkedUsers(username, filter);
    }

    @GetMapping("info/full")
    public Mono<UserFullInfo> getUserFullInfo(@RequestParam String username, @RequestParam SourceType type) {
        return searchService.getUserFullInfo(username, type);
    }

}
