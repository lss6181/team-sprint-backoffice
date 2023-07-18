package com.sparta.sprintbackofficeproject.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.sprintbackofficeproject.exception.ApiException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ApiException apiResult = new ApiException("로그인 성공", HttpStatus.OK.value());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = objectMapper.writeValueAsString(apiResult);

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(jsonResult);
    }
}
