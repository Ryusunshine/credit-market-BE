package com.example.creditmarket.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Image Error
    IMAGE_NOT_FOUND(404, "IMAGE-404-1", "해당 이미지를 찾을 수 없습니다"),

    // Agenda Error
    AGENDA_NOT_FOUND(404, "AGENDA-404-1", "해당 알림장이 존재하지 않습니다."),

    // Dog Eroor
    DOG_NOT_FOUND(404, "DOG-404-1", "해당 강아지가 존재하지 않습니다."),

    // School Error
    SCHOOL_NOT_FOUND(404, "SCHOOL-404-1", "해당 학교가 존재하지 않습니다."),

    // Member Error
    MEMBER_NOT_FOUND(404, "MEMBER-404-1", "해당 회원이 존재하지 않습니다."),

    // Admin Error
    ADMIN_NOT_FOUND_ID(401, "ADMIN-404-1", "해당 ID를 찾을 수 없습니다"),
    ADMIN_NOT_FOUND(404, "ADMIN-404-2", "관리자가 존재하지 않습니다."),
    ADMIN_ID_EXISTS(409, "ADMIN-409-1", "이미 사용중인 아이디입니다."),
    ADMIN_EXISTS(409, "ADMIN-409-2", "이미 등록된 정보입니다."),

    // Auth Error
    AUTH_UNAUTHORIZED(401, "AUTH-401-1", "비밀번호가 일치하지 않습니다"),
    AUTH_DENIED(401, "AUTH-401-2", "승인이 거부되었습니다."),
    AUTH_PENDING(401, "AUTH-401-3", "승인 대기중입니다."),
    AUTH_FORBIDDEN(403, "AUTH-401-2", "관리자만 접근가능합니다."),

    //Token Error
    TOKEN_FOREGED_ERROR(401, "TOKEN-401-1", "잘못된 토큰입니다"),
    TOKEN_EXPIRED_ERROR(401, "TOKEN-410-1", "Expired JWT Token"),

    // 일반적인 에러
    COMMON_BAD_REQUEST(400, "COMMON-400-1", "요청한 값이 올바르지 않습니다"),
    COMMON_CONFLICT(409, "COMMON-409-1", "요청한 값을 처리할 수 없습니다"),
    COMMON_NOT_FOUND(404, "COMMON-404-1", "해당 정보가 없습니다."),
    COMMON_METHOD_NOT_ALLOWED(405, "COMMON-405-1", "허용되지 않은 메소드 입니다"),
    COMMON_INTERNAL_SERVER_ERROR(500, "COMMON-500-1", "일시적인 서버 오류 입니다"),

    // REST ERROR
    REST_SERVER_ERROR(500, "REST-500-1", "통신 서버 오류입니다");

    private final int status;
    private final String code;
    private final String message;

    public String getErrorMessage(Object... arg) {
        return String.format(message, arg);
    }
}


