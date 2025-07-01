package com.example.profilehunter.model.mapper.instagram;

import java.util.Arrays;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum InstagramAttribute {

    TITLE("og:title", startItem -> {
        Pattern pattern = Pattern.compile("[^([a-zA-Z]+ [a-zA-Z]+)]");
        Matcher matcher = pattern.matcher(startItem);

        return matcher.matches() ? matcher.group(0) : null;
    }),
    DESCRIPTION("og:description", item -> item),
    URL("og:url", item -> item),
    IMAGE("og:image", item -> item),
    USERNAME("username", item -> item);

    public final String attributeValue;
    public final UnaryOperator<String> fieldMapFunction;

    InstagramAttribute(String attributeValue, UnaryOperator<String> fieldMapFunction) {
        this.attributeValue = attributeValue;
        this.fieldMapFunction = fieldMapFunction;
    }

    private static final Map<String, InstagramAttribute> BY_ATTRIBUTE;

    static {
        BY_ATTRIBUTE = Arrays.stream(values()).collect(Collectors.toMap(item -> item.attributeValue, item -> item));
    }

    public static InstagramAttribute getByAttribute(String linkType) {
        return BY_ATTRIBUTE.get(linkType);
    }


}
