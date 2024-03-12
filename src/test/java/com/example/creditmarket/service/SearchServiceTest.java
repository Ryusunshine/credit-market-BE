package com.example.creditmarket.service;

import com.example.creditmarket.cache.SearchRedisTemplateService;
import com.example.creditmarket.dto.response.SearchResultDto;
import com.example.creditmarket.entity.EntityOption;
import com.example.creditmarket.repository.OptionRepository;
import com.example.creditmarket.service.Impl.SearchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private SearchRedisTemplateService searchRedisTemplateService;

    @InjectMocks
    private SearchServiceImpl searchService;

    @Test
    void Redis값이_있을시_조회() {
        // when
        List<SearchResultDto> redisData = Arrays.asList(new SearchResultDto(), new SearchResultDto());
        when(searchRedisTemplateService.findAll()).thenReturn(redisData);

        // then
        List<SearchResultDto> result = searchService.getAll();

        // verify
        assertEquals(redisData.size(), result.size());
        verify(searchRedisTemplateService, times(1)).findAll();
        verify(optionRepository, never()).findAll(); // DB 조회 확인
    }

    @Test
    void 레디스_장애발생시_DB에서_조회() {
        // when
        when(searchRedisTemplateService.findAll()).thenReturn(Collections.emptyList());
        List<EntityOption> dbData = Arrays.asList(new EntityOption(), new EntityOption());
        when(optionRepository.findAll()).thenReturn(dbData);

        // then
        List<SearchResultDto> result = searchService.getAll();

        // verify
        assertEquals(dbData.size(), result.size());
        verify(searchRedisTemplateService, times(1)).findAll();
        verify(optionRepository, times(1)).findAll(); // DB에서 데이터를 조회했는지 확인
    }
}