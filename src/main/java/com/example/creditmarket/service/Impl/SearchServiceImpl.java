package com.example.creditmarket.service.Impl;

import com.example.creditmarket.cache.SearchRedisTemplateService;
import com.example.creditmarket.dto.response.MainListResponseDTO;
import com.example.creditmarket.dto.response.SearchResultDto;
import com.example.creditmarket.entity.EntityOption;
import com.example.creditmarket.entity.EntityUser;
import com.example.creditmarket.repository.OptionRepository;
import com.example.creditmarket.repository.UserRepository;
import com.example.creditmarket.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;
    private final SearchRedisTemplateService searchRedisTemplateService;

    @Override
    @Transactional(readOnly = true)
    public List<SearchResultDto> getAll(){
        // redis
        List<SearchResultDto> searchResultDtoList = searchRedisTemplateService.findAll();
        if (!searchResultDtoList.isEmpty()) return searchResultDtoList;

        // db
        return optionRepository.findAll().stream().map(SearchResultDto::of).collect(Collectors.toList());

    }

    /**
     * 키워드와 카테고리 검색 서비스
     * @param keyword : 검색 키워드
     * @param loan : 대출종류
     * @param age : 나이
     * @param gender : 성별
     * @param interest : 금리유형
     * @param rate : 평균금리
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<MainListResponseDTO> search(String keyword, String loan, String age,
                                                  String gender, String interest, Double rate,
                                                  String userId, int page) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Object[]> result;
        EntityUser entityUser;
        if (rate == 0 || rate == null) rate = 20.0;
        if(userId.equals("")) {
            result = optionRepository.search(loan, age, gender, interest, rate, keyword, null, pageable);
        } else {
            entityUser = userRepository.findByUserEmail(userId).get();
            result = optionRepository.search(loan, age, gender, interest, rate, keyword, entityUser, pageable);
        }
        List<Object[]> lists = result.getContent();
        if (lists != null || lists.size() != 0) {
            List<MainListResponseDTO> mainListResponseListDto = new ArrayList<>();
            for (int i = 0; i < lists.size(); i++) {
                EntityOption optioin = (EntityOption)lists.get(i)[0];
                // 해당 유저의 관심상품이 있는 경우
                if(lists.get(i)[1] != null) {
                mainListResponseListDto.add(i, MainListResponseDTO.of(optioin, false));
                } else {
                    mainListResponseListDto.add(i, MainListResponseDTO.of(optioin, true));
                }
            }
            return mainListResponseListDto;
        }
        return null;
    }

}
