package com.example.profilehunter.service.search.tiktok;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.mapper.UserMapperFactory;
import com.example.profilehunter.model.mapper.tiktok.TikTokAttribute;
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
public class TikTokService extends BaseSearchService<Map<TikTokAttribute, String>, Map<TikTokAttribute, String>> {

    private final RestClient restClient;

    @Value("${spring.instagram.link}")
    private String instagramLink;

    public TikTokService(RestClient restClient, UserMapperFactory mapperFactory) {
        super(mapperFactory);
        this.restClient = restClient;
    }

    @Override
    public Function<String, Optional<Map<TikTokAttribute, String>>> searchUser() {
        return item -> {
            String result = restClient.get().uri(instagramLink, item).retrieve().body(String.class);

            Document document = Jsoup.parse(result);

            Supplier<Stream<TikTokAttribute>> attributeStream = () -> Arrays.stream(TikTokAttribute.values());
            Map<TikTokAttribute, String> metaMap = new HashMap<>();

            document.select("meta").forEach(meta -> {
                Optional<TikTokAttribute> metaFieldOpt = attributeStream.get()
                        .filter(attr -> meta.attributes().hasKey("property") && meta.attributes().get("property").equals(attr.name())).findFirst();

                metaFieldOpt.ifPresent(instagramAttribute ->
                        metaMap.put(instagramAttribute, meta.attributes().get("content")));
            });

            return Optional.of(metaMap);
        };
    }

    @Override
    public Function<String, Optional<Map<TikTokAttribute, String>>> searchUserFullInfo() {
        return searchUser();
    }

    @Override
    public Function<String, List<Map<TikTokAttribute, String>>> searchUsers() {
        return item -> {
            Optional<Map<TikTokAttribute, String>> res = searchUser().apply(item);

            return List.of(res.orElse(new HashMap<>()));
        };
    }

    @Override
    public SourceType getSourceType() {
        return SourceType.TIK_TOK;
    }

}
