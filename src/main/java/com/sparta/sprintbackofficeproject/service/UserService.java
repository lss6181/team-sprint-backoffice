package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.dto.ModifyRequestDto;
import com.sparta.sprintbackofficeproject.dto.ModifyResponseDto;
import com.sparta.sprintbackofficeproject.dto.SignupRequestDto;
import com.sparta.sprintbackofficeproject.dto.UserProfileResponseDto;
import com.sparta.sprintbackofficeproject.entity.User;
import com.sparta.sprintbackofficeproject.entity.UserRoleEnum;
import com.sparta.sprintbackofficeproject.repository.UserRepository;
import com.sparta.sprintbackofficeproject.util.EmailAuth;
import com.sparta.sprintbackofficeproject.util.RedisUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailAuth emailAuth;
    private final RedisUtil redisUtil;

    @Value("${user.admin.token}")
    private String ADMIN_TOKEN;

    public void signup(SignupRequestDto requestDto) throws MessagingException {
        String email = requestDto.getEmail();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(requestDto.getUsername());
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 username 입니다.");
        }

        // email 중복확인
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        redisUtil.saveSignupRequestDto(email, requestDto, 300);

        emailAuth.sendEmail(email);
    }

    public Boolean verifyCode(String email, String code) {
        String codeFindByEmail = redisUtil.getData(email);
        if (codeFindByEmail == null) {
            return false;
        }
        return codeFindByEmail.equals(code);
    }

    @Transactional
    public void saveUserAfterVerify(String email) {
        SignupRequestDto requestDto = redisUtil.getClass(email, SignupRequestDto.class).orElse(null);
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String userEmail = requestDto.getEmail();
        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        // 사용자 등록
        User user = new User(username, password, userEmail, role);
        userRepository.save(user);
    }

    // 유저 프로필 조회
    public UserProfileResponseDto getUserProfile(Long userId) {
        User targetUser = findUser(userId);
        return new UserProfileResponseDto(targetUser.getId(),
                targetUser.getUsername(), targetUser.getEmail(), targetUser.getImageUrl(), targetUser.getIntroduction());
    }

    // 유저 정보 수정
    @Transactional
    public ModifyResponseDto modifyProfile(User user, ModifyRequestDto modifyRequestDto, Long userId) {
        User targetUser = findUser(userId);

        // 유저 정보 수정은 관리자 혹은 유저 본인만 수정 가능
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || targetUser.getId().equals(user.getId()))) {
            throw new RejectedExecutionException();
        } else {
            targetUser.modifyProfile(modifyRequestDto);
            return new ModifyResponseDto("프로필 변경이 완료되었습니다.", 201);
        }
    }

    // 유저 찾기
    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
        );
    }
}