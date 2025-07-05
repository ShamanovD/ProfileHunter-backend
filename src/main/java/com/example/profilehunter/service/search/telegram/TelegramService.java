package com.example.profilehunter.service.search.telegram;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.TelegramInfoWrapper;
import com.example.profilehunter.model.mapper.UserMapperFactory;
import com.example.profilehunter.service.search.BaseSearchService;
import dev.voroby.springframework.telegram.client.TelegramClient;
import dev.voroby.springframework.telegram.client.templates.response.Response;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

@Service
public class TelegramService extends BaseSearchService<TdApi.User, TelegramInfoWrapper> {

    private final TelegramClient client;

    public TelegramService(UserMapperFactory mapperFactory, TelegramClient client) {
        super(mapperFactory);
        this.client = client;
    }

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
    public Function<String, Optional<TelegramInfoWrapper>> searchUserFullInfo() {
        return item -> {
            TelegramInfoWrapper wrapper = new TelegramInfoWrapper();

            Optional<Long> id = getUserIdByUserName(item);

            if (id.isPresent()) {
                getUserById(id.get()).ifPresent(wrapper::setUser);

                Optional<TdApi.ChatPhotos> chatPhotos = client.send(
                        new TdApi.GetUserProfilePhotos(id.get(), 0, 5))
                        .getObject();

                chatPhotos.ifPresent(wrapper::setPhotos);// TODO: 05.07.2025 add AWS support for photos, maybe in future split to another microservice
            }

            return Optional.of(wrapper);
        };
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
    public SourceType getSourceType() {
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

}
