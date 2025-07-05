package com.example.profilehunter.endpoint;

import com.example.profilehunter.service.dictionary.IDictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("dictionary")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", methods = { RequestMethod.GET, RequestMethod.POST })
public class DictionaryController {

    private final IDictionaryService dictionaryService;

    @GetMapping("fieldDictionary")
    public Map<String, String> getFieldDictionary() {
        return dictionaryService.getFieldDictionary();
    }

}
