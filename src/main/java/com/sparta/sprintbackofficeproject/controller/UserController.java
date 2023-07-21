package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.dto.*;
import com.sparta.sprintbackofficeproject.exception.ApiException;
import com.sparta.sprintbackofficeproject.security.UserDetailsImpl;
import com.sparta.sprintbackofficeproject.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiException> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) throws MessagingException {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : fieldErrors) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
                return ResponseEntity.badRequest().body(
                        new ApiException(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage(), HttpStatus.BAD_REQUEST.value())
                );
            }
        }
        userService.signup(requestDto);
        return ResponseEntity.ok().body(new ApiException("인증 메일이 발송되었습니다.", HttpStatus.OK.value()));
    }

    @PostMapping("/signup/email-auth")
    public ResponseEntity<ApiException> verifyCode(@RequestBody EmailRequestDto requestDto) throws IOException {
        if (userService.verifyCode(requestDto.getEmail(), requestDto.getCode())) {

            userService.saveUserAfterVerify(requestDto.getEmail());
            return ResponseEntity.ok().body(new ApiException("회원가입 완료", HttpStatus.OK.value()));
        }
        return ResponseEntity.badRequest().body(new ApiException("인증 코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value()));
    }


    // 프로필 조회
    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiException> getUserProfile(@PathVariable Long userId) {
        UserProfileResponseDto result = userService.getUserProfile(userId);
        return ResponseEntity.ok().body(result);
    }

    // 프로필 수정
    @PutMapping("/profile/{userId}")
    public ResponseEntity<ApiException> modifyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable Long userId,
                                                      @RequestPart(value = "requestDto") ModifyRequestDto requestDto,
                                                      @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        ModifyResponseDto result = userService.modifyProfile(userDetails.getUser(), userId, requestDto, file);
        return ResponseEntity.ok().body(result);
    }
}