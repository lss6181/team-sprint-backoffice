package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.dto.*;
import com.sparta.sprintbackofficeproject.entity.Follow;
import com.sparta.sprintbackofficeproject.entity.User;
import com.sparta.sprintbackofficeproject.entity.UserRoleEnum;
import com.sparta.sprintbackofficeproject.repository.FollowRepository;
import com.sparta.sprintbackofficeproject.repository.LikePostRepository;
import com.sparta.sprintbackofficeproject.repository.UserRepository;
import com.sparta.sprintbackofficeproject.util.EmailAuth;
import com.sparta.sprintbackofficeproject.util.RedisUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final LikePostRepository likePostRepository;
    private final FileUploadService fileUploadService;
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

        // targetUser의 팔로잉
        List<String> followingUsers = new ArrayList<>();
        List<Follow> followings = followRepository.findAllByFollowerUser(targetUser);
        for (Follow follow : followings) {
            followingUsers.add(follow.getFollowingUser().getUsername());
        }

        // targetUser의 팔로워
        List<String> followerUsers = new ArrayList<>();
        List<Follow> followers = followRepository.findAllByFollowingUser(targetUser);
        for (Follow follow : followers) {
            followerUsers.add(follow.getFollowerUser().getUsername());
        }

        return new UserProfileResponseDto(targetUser.getId(),
                targetUser.getUsername(), targetUser.getEmail(), targetUser.getImageUrl(), targetUser.getIntroduction(), followingUsers, followerUsers);
    }

    // 유저 정보 수정
    @Transactional
    public ModifyResponseDto modifyProfile(User user, Long userId, ModifyRequestDto modifyRequestDto, MultipartFile file) throws IOException {
        User targetUser = findUser(userId);

        // 유저 정보 수정은 관리자 혹은 유저 본인만 수정 가능
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || targetUser.getId().equals(user.getId()))) {
            throw new RejectedExecutionException();
        } else {
            if (file != null) {  // 수정 내용중 사진 수정이 있을경우
                if (targetUser.getImageUrl() != null && targetUser.getImageUrl() != "") { // 기존에 사진이 존재할 경우
                    fileUploadService.deleteFile(targetUser.getImageUrl()); //사진 삭제
                }
            }
            String imageUrl = fileUploadService.uploadFile(file); // 파일 업로드
            String encodedPassword = passwordEncoder.encode(modifyRequestDto.getPassword());
            modifyRequestDto.setImageUrl(imageUrl);
            modifyRequestDto.setPassword(encodedPassword);

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

    // 유저 정보 조회
    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. : " + username));
    }

    // 유저 정보 수정
    public User modifyUser(Long userId, ModifyRequestDto modifyRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 ID를 찾을 수 없습니다. : " + userId));
        user.modifyProfile(modifyRequestDto);
        return userRepository.save(user);
    }

    // 유저 삭제
    @Transactional
    public User deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 ID를 찾을 수 없습니다. : " + userId));
        likePostRepository.deleteByUserId(userId);
        userRepository.delete(user);
        return user;
    }

    // 유저 관리자 승격
    public User promoteToAdmin(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 ID를 찾을 수 없습니다. : " + id));
        user.upgradeToAdmin();
        userRepository.save(user);
        return user;
    }

    // 유저 차단
    public void blockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 ID를 찾을 수 없습니다. : " + id));
        user.block();
        userRepository.save(user);
    }

    public void unblockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 ID를 찾을 수 없습니다. : " + id));
        user.unblock();
        userRepository.save(user);
    }
}