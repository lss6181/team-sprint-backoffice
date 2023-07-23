package com.sparta.sprintbackofficeproject.socialLogin;

import com.sparta.sprintbackofficeproject.entity.UserRoleEnum;
import com.sparta.sprintbackofficeproject.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.net.URLEncoder;

@Slf4j(topic = "OAuth2 Login")
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String username = (String) ((OAuth2User) authentication.getPrincipal()).getAttributes().get("name");
        UserRoleEnum role = UserRoleEnum.USER;
        String token = jwtUtil.createToken(username, role);
        token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

        Cookie cookie = new Cookie(jwtUtil.AUTHORIZATION_HEADER, token); // 쿠키 생성, 토큰 담기
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect("/");
        log.info("로그인 성공");
    }
}