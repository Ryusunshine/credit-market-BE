package com.example.creditmarket.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private String code;

    public static ErrorResponse of(int status, String message, String errorCode) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .code(errorCode)
                .build();
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();
    }
}
