package com.example.creditmarket.controller;

import com.example.creditmarket.dto.request.UserLoginRequestDTO;
import com.example.creditmarket.dto.request.UserSignUpRequestDTO;
import com.example.creditmarket.dto.response.LoginResponseDTO;
import com.example.creditmarket.dto.response.UserInfoResponseDTO;
import com.example.creditmarket.entity.EntityUser;
import com.example.creditmarket.service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody UserSignUpRequestDTO request) {
        userService.signup(request);
        return ResponseEntity.ok().body("SIGNUP_SUCCESS");
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserLoginRequestDTO request) {
        LoginResponseDTO loginReturn = userService.login(request.getUserEmail(), request.getUserPassword());
        return ResponseEntity.ok().body(loginReturn);
    }

    @PostMapping("authorizationTest")
    public ResponseEntity<String> review(@RequestBody Authentication authentication) {
        return ResponseEntity.ok().body(authentication.getName() + " SUCCESS");
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok().body("LOGOUT_SUCCESS");
    }

    @PostMapping("passwordcheck")
    public ResponseEntity<UserInfoResponseDTO> userpasswordCheck(@RequestBody UserLoginRequestDTO request) {
        UserInfoResponseDTO user = userService.passwordCheck(request.getUserEmail(), request.getUserPassword());
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("info/update")
    public ResponseEntity<String> infoUpdate(@RequestBody UserSignUpRequestDTO request) {
        userService.infoUpdate(request.toEntity());
        return ResponseEntity.ok().body("INFO_UPDATE_SUCCESS");
    }

    @GetMapping("info")
    public ResponseEntity<EntityUser> getUserInfo(HttpServletRequest request) {
        EntityUser user = userService.getUserInfo(request);
        return ResponseEntity.ok().body(user);
    }


}
