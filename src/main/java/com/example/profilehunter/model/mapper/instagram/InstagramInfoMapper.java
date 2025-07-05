package com.example.profilehunter.model.mapper.instagram;

import com.example.profilehunter.model.common.SourceType;
import com.example.profilehunter.model.dto.UserFullInfo;
import com.example.profilehunter.model.dto.UserInfo;
import com.example.profilehunter.model.mapper.BaseUserInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = List.class)
public abstract class InstagramInfoMapper extends BaseUserInfoMapper<Map<InstagramAttribute, String>, Map<InstagramAttribute, String>> {

    @Mapping(target = "description", expression = "java(getElementFromMap(elementMap, InstagramAttribute.DESCRIPTION))")
    @Mapping(target = "image", expression = "java(getElementFromMap(elementMap, InstagramAttribute.IMAGE))")
    @Mapping(target = "url", expression = "java(getElementFromMap(elementMap, InstagramAttribute.URL))")
    @Mapping(target = "username", expression = "java(getElementFromMap(elementMap, InstagramAttribute.USERNAME))")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "id", ignore = true)
    public abstract UserInfo mapUser(Map<InstagramAttribute, String> elementMap);

    @Mapping(target = "description", expression = "java(getElementFromMap(elementMap, InstagramAttribute.DESCRIPTION))")
    @Mapping(target = "image", expression = "java(getElementFromMap(elementMap, InstagramAttribute.IMAGE))")
    @Mapping(target = "url", expression = "java(getElementFromMap(elementMap, InstagramAttribute.URL))")
    @Mapping(target = "username", expression = "java(getElementFromMap(elementMap, InstagramAttribute.USERNAME))")
    @Mapping(target = "profilePhotos", expression = "java(List.of(getElementFromMap(elementMap, InstagramAttribute.IMAGE)))")
    @Mapping(target = "sourceType", expression = "java(getSourceType())")
    @Mapping(target = "id", ignore = true)
    public abstract UserFullInfo mapUserWithFullInfo(Map<InstagramAttribute, String> elementMap);

    @Override
    public SourceType getSourceType() {
        return SourceType.INSTAGRAM;
    }

    protected String getElementFromMap(Map<InstagramAttribute, String> elementMap, InstagramAttribute key) {
        if (elementMap.containsKey(key)) {
            return key.fieldMapFunction.apply(elementMap.get(key));
        }

        return null;
    }

    @AfterMapping
    protected void setName(@MappingTarget UserInfo userInfo, Map<InstagramAttribute, String> elementMap) {
        if (elementMap.containsKey(InstagramAttribute.TITLE)) {
            String enumMapString = InstagramAttribute.TITLE.fieldMapFunction.apply(elementMap.get(InstagramAttribute.TITLE));

            if (Objects.nonNull(enumMapString)) {
                String[] splitItems = enumMapString.split(StringUtils.SPACE);

                userInfo.setFirstName(splitItems.length > 0 ? splitItems[0] : null);
                userInfo.setLastName(splitItems.length > 1 ? splitItems[1] : null);
            }

        }
    }

}
