package com.example.creditmarket.service;

import com.example.creditmarket.dto.request.UserSignUpRequestDTO;
import com.example.creditmarket.dto.response.LoginResponseDTO;
import com.example.creditmarket.dto.response.UserInfoResponseDTO;
import com.example.creditmarket.entity.EntityUser;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    String signup(UserSignUpRequestDTO request);

    LoginResponseDTO login(String userEmail, String password);

    Boolean isValid(String userToken);

    String logout(HttpServletRequest request);

    UserInfoResponseDTO passwordCheck(String userEmail, String password);

    String infoUpdate(EntityUser user);

    EntityUser getUserInfo(HttpServletRequest request);

}
