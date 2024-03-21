package com.example.creditmarket.dto.request;

import com.example.creditmarket.domain.entity.EntityUser;
import lombok.*;

@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserSignUpRequestDTO {
    private String userEmail;
    private String userPassword;
    private String userName;
    private String userGender;
    private String userBirthDate;
    private String userJob;
    private String userPrefCreditProductTypeName;
    private String userPrefInterestType;
    private Long userCreditScore;

    public EntityUser toEntity(){
        return EntityUser.builder()
                .userEmail(userEmail)
                .userPassword(userPassword)
                .userName(userName)
                .userGender(userGender)
                .userBirthdate(userBirthDate)
                .userJob(userJob)
                .userPrefCreditProductTypeName(userPrefCreditProductTypeName)
                .userPrefInterestType(userPrefInterestType)
                .userCreditScore(userCreditScore)
                .build();
    }

}
