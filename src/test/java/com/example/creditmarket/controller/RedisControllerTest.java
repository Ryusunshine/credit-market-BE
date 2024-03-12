package com.example.creditmarket.controller;

import com.example.creditmarket.cache.SearchRedisTemplateService;
import com.example.creditmarket.dto.response.ProductDetailResponseDTO;
import com.example.creditmarket.dto.response.SearchResultDto;
import com.example.creditmarket.entity.EntityOption;
import com.example.creditmarket.repository.FProductRepository;
import com.example.creditmarket.repository.OptionRepository;
import com.example.creditmarket.service.Impl.SearchServiceImpl;
import com.example.creditmarket.service.SearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class RedisControllerTest {
    @Autowired
    private SearchService searchService;

    @Autowired
    private SearchRedisTemplateService searchRedisTemplateService;

    @Test
    public void 전체_조회_객체를_Redis에_저장() {
        List<SearchResultDto> list = searchService.getAll();
        list.forEach(searchRedisTemplateService::save);
    }

    @Test
    public void Redis에서_전체_상품_조회(){
        //when
        List<SearchResultDto> redisResult = searchRedisTemplateService.findAll();
        List<SearchResultDto> dbResult = searchService.getAll();

        //then
        Assertions.assertEquals(redisResult.size(), dbResult.size());

    }

}
