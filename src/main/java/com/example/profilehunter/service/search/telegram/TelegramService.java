package com.example.profilehunter.service.search.telegram;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.mapper.telegram.TelegramInfoMapper;
import com.example.profilehunter.service.search.BaseSearchService;
import dev.voroby.springframework.telegram.client.TelegramClient;
import dev.voroby.springframework.telegram.client.templates.response.Response;
import lombok.RequiredArgsConstructor;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TelegramService extends BaseSearchService<TdApi.User, TdApi.User> {

    private final TelegramClient client;

    private final TelegramInfoMapper mapper;

    @Override
    public Function<String, Optional<TdApi.User>> searchUser() {
        return item -> {
            Optional<Long> id = getUserIdByUserName(item);

            if (id.isPresent()) {
                return getUserById(id.get());
            }

            return Optional.empty();
        };
    }

    @Override
    public Function<String, Optional<TdApi.User>> searchUserFullInfo() {
        return searchUser();
    }

    @Override
    public Function<String, List<TdApi.User>> searchUsers() {
        return item -> {
            List<Long> ids = getUsersIdsByUsername(item);
            Optional<Long> id = getUserIdByUserName(item);

            id.ifPresent(ids::add);

            return ids.stream()
                    .map(this::getUserById)
                    .filter(Optional::isPresent)
                    .map(Optional::get).toList();
        };
    }

    @Override
    public Function<TdApi.User, UserInfo> mapUser() {
        return mapper::mapUser;
    }

    @Override
    public Function<TdApi.User, UserFullInfo> mapUserWithFullInfo() {
        return mapper::mapUserWithFullInfo;
    }

    @Override
    public SourceType getSearchType() {
        return SourceType.TELEGRAM;
    }

    private Optional<Long> getUserIdByUserName(String userName) {
        Response<TdApi.Chat> chat = client.send(new TdApi.SearchPublicChat(userName));

        AtomicReference<Long> id = new AtomicReference<>(0L);
        chat.getObject().ifPresent(item -> id.set(item.id));

        return Optional.ofNullable(id.get());
    }

    private List<Long> getUsersIdsByUsername(String username) {
        Response<TdApi.Chats> chats = client.send(new TdApi.SearchPublicChats(username));

        if (chats.getObject().isPresent()) {
            return Arrays.stream(chats.getObject().get().chatIds).boxed().toList();
        }

        return new ArrayList<>();
    }

    private Optional<TdApi.User> getUserById(Long id) {
        return client.send(new TdApi.GetUser(id)).getObject();
    }

    private Optional<TdApi.UserFullInfo> getUserFullInfoById(Long id) {
        return client.send(new TdApi.GetUserFullInfo(id)).getObject();
    }
}
