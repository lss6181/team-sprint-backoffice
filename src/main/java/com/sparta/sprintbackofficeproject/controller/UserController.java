package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.dto.EmailRequestDto;
import com.sparta.sprintbackofficeproject.dto.SignupRequestDto;
import com.sparta.sprintbackofficeproject.entity.User;
import com.sparta.sprintbackofficeproject.exception.ApiException;
import com.sparta.sprintbackofficeproject.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiException> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size()> 0 ){
            for (FieldError fieldError : fieldErrors){
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
                return ResponseEntity.badRequest().body(
                        new ApiException(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage(), HttpStatus.BAD_REQUEST.value())
                );
            }
        }
        userService.signup(requestDto);
        return ResponseEntity.ok().body(new ApiException("회원가입 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/signup/email-auth")
    public void emailAuth(@RequestBody EmailRequestDto requestDto) throws MessagingException {
        userService.sendEmail(requestDto.getEmail());
    }

    /// 백오피스 기능입니다.

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}