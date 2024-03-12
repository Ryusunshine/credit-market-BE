package com.example.creditmarket.dto.request;

import lombok.*;

@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserLoginRequestDTO {

    private String userEmail;
    private String userPassword;
}
