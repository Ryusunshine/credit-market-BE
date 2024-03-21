package com.example.creditmarket.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    NEW_PRODUCT("새로운 금융상품!"),
    NEW_RECOMMEND("새로운 추천상품!"),
    NEW_COMMENT("새로운 댓글!");

    private final String alarmText;
}
