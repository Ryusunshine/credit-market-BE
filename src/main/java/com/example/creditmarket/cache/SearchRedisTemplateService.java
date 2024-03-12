package com.example.creditmarket.cache;

import com.example.creditmarket.dto.response.MainListResponseDTO;
import com.example.creditmarket.dto.response.SearchResultDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchRedisTemplateService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CACHE_KEY = "PRODUCT";

    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(SearchResultDto resultDto) {
        if(Objects.isNull(resultDto) || Objects.isNull(resultDto.getProductId())) {
            log.error("Required Values must not be null");
            return;
        }

        try {
            hashOperations.put(CACHE_KEY,
                    resultDto.getProductId(),
                    serializeDto(resultDto));
            log.info("[RedisTemplateService save success] id: {}", resultDto.getProductId());
        } catch (Exception e) {
            log.error("[RedisTemplateService save error] {}", e.getMessage());
        }
    }

    public List<SearchResultDto> findAll() {

        try {
            List<SearchResultDto> list = new ArrayList<>();
            for (String value : hashOperations.entries(CACHE_KEY).values()) {
                SearchResultDto resultDto = deserializeDto(value);
                list.add(resultDto);
            }
            return list;

        } catch (Exception e) {
            log.error("[RedisTemplateService findAll error]: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void delete(String id) {
        hashOperations.delete(CACHE_KEY, id);
        log.info("[RedisTemplateService delete]: {} ", id);
    }

    private String serializeDto(SearchResultDto productDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(productDto);
    }

    private SearchResultDto deserializeDto(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, SearchResultDto.class);
    }

}
