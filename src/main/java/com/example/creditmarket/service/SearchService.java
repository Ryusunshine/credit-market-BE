package com.example.creditmarket.service;

import com.example.creditmarket.dto.response.MainListResponseDTO;
import com.example.creditmarket.dto.response.SearchResultDto;

import java.util.List;

public interface SearchService {

    List<MainListResponseDTO> search(String keyword, String loan, String age,
                                                  String gender, String interest, Double rate,
                                                  String userId, int page);

    List<SearchResultDto> getAll();
}
