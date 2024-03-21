package com.example.creditmarket.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Table(name = "tb_user")
@NoArgsConstructor
@Getter
@Builder
public class EntityUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String userEmail;

    private String userPassword;

    private String userName;

    private String userGender;

    private String userBirthdate;

    private String userJob;

    private String userPrefCreditProductTypeName;

    private String userPrefInterestType;

    private Long userCreditScore;

    private String image;

    public void setImg(String image){
        this.image = image;
    }

}
