package com.example.creditmarket.controller;

import com.example.creditmarket.dto.response.MainListResponseDTO;
import com.example.creditmarket.dto.response.SearchResultDto;
import com.example.creditmarket.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import io.swagger.annotations.Api;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"검색 서비스"}, description = "키워드와 카테고리 검색을 담당합니다.")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/main")
    public ResponseEntity<List<SearchResultDto>> getAll(){
        return ResponseEntity.ok().body(searchService.getAll());
    }

    @GetMapping("/search/results")
    public ResponseEntity<List<MainListResponseDTO>> searchKeyword(@RequestParam(required = false, defaultValue = "") @Nullable String keyword,
                                                   @RequestParam(required = false, defaultValue = "") @Nullable String loan,
                                                   @RequestParam(required = false, defaultValue = "") @Nullable String age,
                                                   @RequestParam(required = false, defaultValue = "") @Nullable String gender,
                                                   @RequestParam(required = false, defaultValue = "대출금리") @Nullable String interest,
                                                   @RequestParam(required = false, defaultValue = "0") @Nullable Double rate,
                                                   Authentication authentication,
                                                   @RequestParam(required = false, defaultValue = "1") int page
                                                ) {
        List<MainListResponseDTO> result;
        String userEmail = "";
        if(authentication != null){
            userEmail = authentication.getPrincipal().toString();
            result = searchService.search(keyword.trim(), loan.trim(), age.trim(), gender.trim(), interest.trim(), rate, userEmail, (page-1));
        } else {
            result = searchService.search(keyword.trim(), loan.trim(), age.trim(), gender.trim(), interest.trim(), rate, userEmail, (page-1));
        }
        return ResponseEntity.ok().body(result);
    }

}
