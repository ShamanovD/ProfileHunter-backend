package com.example.profilehunter.service.dictionary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DictionaryService implements IDictionaryService {

    @Override
    public Map<String, String> getFieldDictionary() {
        return readDictionary("fieldName.json", new TypeReference<>() {});
    }

    private <T> T readDictionary(String fileName, TypeReference<T> typeRef) {
        Resource resource = new ClassPathResource(fileName);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(resource.getInputStream(), objectMapper.getTypeFactory().constructType(typeRef));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
