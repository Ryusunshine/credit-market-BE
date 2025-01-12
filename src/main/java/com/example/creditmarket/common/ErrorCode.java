package com.example.creditmarket.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USERMAIL_DUPLICATED(HttpStatus.CONFLICT, ""),
    USERMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, ""),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    PAGE_INDEX_ZERO(HttpStatus.BAD_REQUEST, ""),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    COMMON_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    DATABASE_ERROR(HttpStatus.CONFLICT, ""),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    NOTIFICATION_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "");

    private HttpStatus httpStatus;
    private String message;
}
