package com.example.creditmarket.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderListResponseDTO {
    private final List<OrderResponseDTO> list;
    private final int totalNum;

    @Builder
    public OrderListResponseDTO(List<OrderResponseDTO> list, int totalNum) {
        this.list = list;
        this.totalNum = totalNum;
    }
}
