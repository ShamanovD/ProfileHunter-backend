package com.example.profilehunter.service.search.meta.instagram;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.mapper.UserMapperFactory;
import com.example.profilehunter.model.mapper.instagram.InstagramAttribute;
import com.example.profilehunter.service.search.BaseSearchService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class InstagramService extends BaseSearchService<Map<InstagramAttribute, String>, Map<InstagramAttribute, String>> {

    private final RestClient restClient;

    @Value("${spring.instagram.link}")
    private String instagramLink;

    public InstagramService(RestClient restClient, UserMapperFactory mapperFactory) {
        super(mapperFactory);
        this.restClient = restClient;
    }

    @Override
    public Function<String, Optional<Map<InstagramAttribute, String>>> searchUser() {
        return item -> {
            String result = restClient.get().uri(instagramLink, item).retrieve().body(String.class);

            Document document = Jsoup.parse(result);

            Supplier<Stream<InstagramAttribute>> attributeStream = () -> Arrays.stream(InstagramAttribute.values());
            Map<InstagramAttribute, String> metaMap = new HashMap<>();

            document.select("meta").forEach(meta -> {
                Optional<InstagramAttribute> metaFieldOpt = attributeStream.get()
                        .filter(attr -> meta.attributes().hasKey("property") && meta.attributes().get("property").equals(attr.attributeValue)).findFirst();

                metaFieldOpt.ifPresent(instagramAttribute ->
                        metaMap.put(instagramAttribute, meta.attributes().get("content")));
            });

            if (!metaMap.isEmpty()) {
                metaMap.put(InstagramAttribute.USERNAME, item);
            }

            return Optional.of(metaMap);
        };
    }

    @Override
    public Function<String, Optional<Map<InstagramAttribute, String>>> searchUserFullInfo() {
        return searchUser();
    }

    @Override
    public Function<String, List<Map<InstagramAttribute, String>>> searchUsers() {
        return item -> {
            Optional<Map<InstagramAttribute, String>> res = searchUser().apply(item);

            if (res.isPresent() && !res.get().isEmpty()) {
                res.get().put(InstagramAttribute.USERNAME, item);
            }

            return List.of(res.orElse(new HashMap<>()));
        };
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.INSTAGRAM;
    }
}
