package com.example.creditmarket.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FavoriteListResponseDTO {
    private final List<FavoriteResponseDTO> list;
    private final int totalNum;

    @Builder
    public FavoriteListResponseDTO(List<FavoriteResponseDTO> list, int totalNum) {
        this.list = list;
        this.totalNum = totalNum;
    }
}
