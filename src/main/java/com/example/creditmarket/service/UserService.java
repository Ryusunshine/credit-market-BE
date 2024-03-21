package com.example.creditmarket.service;

import com.example.creditmarket.domain.entity.EntityUser;
import com.example.creditmarket.dto.request.UserSignUpRequestDTO;
import com.example.creditmarket.dto.response.AlarmReponse;
import com.example.creditmarket.dto.response.LoginResponseDTO;
import com.example.creditmarket.dto.response.UserInfoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    String signup(UserSignUpRequestDTO request);

    LoginResponseDTO login(String userEmail, String password);

    Boolean isValid(String userToken);

    String logout(HttpServletRequest request);

    UserInfoResponseDTO passwordCheck(String userEmail, String password);

    String infoUpdate(EntityUser user);

    EntityUser getUserInfo(HttpServletRequest request);

    Page<AlarmReponse> getAlarmList(Long userId, Pageable pageable);
}
