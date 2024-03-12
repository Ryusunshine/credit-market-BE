package com.example.creditmarket.dto.response;

import lombok.Getter;

@Getter
public class LoginResponseDTO {
    private final String userName;
    private final String token;

    public LoginResponseDTO(String userName, String token){
        this.userName = userName;
        this.token = token;
    }

}
