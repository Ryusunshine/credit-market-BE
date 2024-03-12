package com.example.creditmarket.dto.response;

import com.example.creditmarket.entity.EntityUser;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponseDTO {
    private String email;
    private String password;
    private String gender;
    private String birthDate;
    private String job;
    private String prefCreditProductTypeName;
    private String prefInterestType;
    private Long creditScore;


    public static UserInfoResponseDTO of(EntityUser user){
        return UserInfoResponseDTO.builder()
                .email(user.getUserEmail())
                .password(user.getUserPassword())
                .gender(user.getUserGender())
                .birthDate(user.getUserBirthdate())
                .prefCreditProductTypeName(user.getUserPrefCreditProductTypeName())
                .prefInterestType(user.getUserPrefInterestType())
                .creditScore(user.getUserCreditScore())
                .build();
    }

}
