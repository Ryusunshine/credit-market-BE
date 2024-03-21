package com.example.creditmarket.config;

import com.example.creditmarket.common.JwtUtil;
import com.example.creditmarket.service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserServiceImpl userServiceImpl;
    private final String secretKey;

    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/user/alarm/subscribe");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token;
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization : {}", authorization);
        if (TOKEN_IN_PARAM_URLS.contains(request.getRequestURI())) {
            log.info("Request with {} check the query param", request.getRequestURI());
            token = request.getQueryString().split("=")[1].trim();
        } else if(authorization == null || !authorization.startsWith("Bearer")){
            log.error("authorization is null or not Bearer");
            filterChain.doFilter(request, response);
            return;
        } else {
            token = authorization.split(" ")[1].trim();
        }

        if (authorization.split(" ").length != 2) {
            log.error("Token is not valid");
            filterChain.doFilter(request, response);
            return;
        }


        // 토큰 유효성 검사
        if (!userServiceImpl.isValid(token)) {
            log.error("Logged out user");
            filterChain.doFilter(request, response);
            return;
        }

        // UserName Token에서 꺼내기
        String userName = JwtUtil.getUserEmail(token, secretKey);
        log.info("userName : {}", userName);

        //권한 부여
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority("USER")));

        //Detail을 넣어줍니다.
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
