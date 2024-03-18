package com.example.creditmarket.service.Impl;

import com.example.creditmarket.dto.request.UserSignUpRequestDTO;
import com.example.creditmarket.dto.response.LoginResponseDTO;
import com.example.creditmarket.dto.response.UserInfoResponseDTO;
import com.example.creditmarket.entity.EntityUser;
import com.example.creditmarket.exception.AppException;
import com.example.creditmarket.exception.ErrorCode;
import com.example.creditmarket.repository.UserRepository;
import com.example.creditmarket.service.UserService;
import com.example.creditmarket.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final StringRedisTemplate redisTemplate;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private Long expiredMs = 1000 * 60 * 60 * 24 * 7L; //일주일

    @Value("C:/Users/profile/img/")
    private String uploadDir;

    @Override
    public String signup(UserSignUpRequestDTO request) {

        //userEmail 중복 체크
        userRepository.findByUserEmail(request.getUserEmail())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERMAIL_DUPLICATED, "이미 존재하는 이메일입니다.");
                });

        //저장
        EntityUser EncodedEntityUser = EntityUser.builder()
                .userEmail(request.getUserEmail())
                .userName(request.getUserName())
                .userPassword(encoder.encode(request.getUserPassword())) //Encoded
                .userGender(request.getUserGender())
                .userBirthdate(request.getUserBirthDate())
                .userJob(request.getUserJob())
                .userPrefCreditProductTypeName(request.getUserPrefCreditProductTypeName())
                .userPrefInterestType(request.getUserPrefInterestType())
                .userCreditScore(request.getUserCreditScore())
                .build();

        userRepository.save(EncodedEntityUser);
        return "success";
    }

    @Override
    public LoginResponseDTO login(String userEmail, String password) {
        //userEmail 없음
        EntityUser selectedUser = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));

        //password 틀림
        if (!encoder.matches(password, selectedUser.getUserPassword())) { //순서 중요. inputpassword, DBpassword
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");
        }
        String token = JwtUtil.createToken(selectedUser.getUserEmail(), secretKey, expiredMs);
        // 레디스에 키 벨류 형식으로 회원 이메일, 토큰 저장
        redisTemplate.opsForValue().set("RT:" + userEmail, token);
        return new LoginResponseDTO(selectedUser.getUserName(), token);
    }

    @Override
    public Boolean isValid(String userToken) {
        try {
            ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
            if(logoutValueOperations.get(userToken) != null){
                System.out.println("로그아웃된 토큰 입니다.");
                return false;
            }
            return !Jwts.parser().setSigningKey(secretKey).parseClaimsJws(userToken)
                    .getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            e.getMessage();
            return false;
        }
    }

    @Override
    public String logout(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1].trim();
        String userEmail = JwtUtil.getUserEmail(token, secretKey);
        if (redisTemplate.opsForValue().get("RT:" + userEmail)!= null) {
            redisTemplate.delete("RT:" + userEmail);}
        redisTemplate.opsForValue().set(token, "logout");
        return "LOGOUT_SUCCESS";
    }

    @Override
    public UserInfoResponseDTO passwordCheck(String userEmail, String password) {
        //userEmail 없음
        EntityUser user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));

        //password 틀림
        if (!encoder.matches(password, user.getUserPassword())) { //순서 중요. inputpassword, DBpassword
            throw new AppException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");
        }
        return UserInfoResponseDTO.of(user);
    }

    @Override
    public String infoUpdate(EntityUser user) {
        userRepository.save(user);
        return "success";
    }

    @Override
    public EntityUser getUserInfo(HttpServletRequest request) {
        // userToken 없음
        // Token 꺼내기
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1].trim();
        String userEmail = JwtUtil.getUserEmail(token, secretKey);
        EntityUser selectedUser = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));
        return selectedUser;
    }
//
//    @Override
//    public String updateImg(String userEmail, MultipartFile file) {
//        if (file == null){
//            throw new AppException(ErrorCode.COMMON_NOT_FOUND, "파일이 없습니다");
//        }
//
//        try {
//            String image = uploadPic(file);
//            EntityUser selectedUser = userRepository.findByUserEmail(userEmail)
//                    .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));
//
//            selectedUser.setImg(image);
//
//        } catch (IOException e) {
//            throw new AppException(ErrorCode.DATABASE_ERROR, "사진 저장에 실패했습니다");
//        }
//
//        return "success";
//    }
//
//
//    /*
//    파일 업로드 관련 메서드
//     */
//    private String uploadPic(MultipartFile file) throws IOException {
//        Path uploadPath = Paths.get(uploadDir);
//        if (!Files.isDirectory(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//        UUID uuid = UUID.randomUUID(); // 중복 방지를 위한 랜덤 값
//        String originFileName = file.getOriginalFilename(); //파일 원래 이름
//        String fullPath = uploadDir + uuid.toString() + "_" + originFileName;
//        file.transferTo(new File(fullPath));
//
//        return fullPath;
//    }


}
