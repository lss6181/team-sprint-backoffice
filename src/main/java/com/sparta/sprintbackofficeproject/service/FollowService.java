package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.entity.Follow;
import com.sparta.sprintbackofficeproject.entity.User;
import com.sparta.sprintbackofficeproject.exception.ApiException;
import com.sparta.sprintbackofficeproject.repository.FollowRepository;
import com.sparta.sprintbackofficeproject.repository.UserRepository;
import com.sparta.sprintbackofficeproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {
	private final UserRepository userRepository;
	private final FollowRepository followRepository;


	@Transactional
	public ResponseEntity<ApiException> following(UserDetailsImpl userDetails, Long followingId) {
		// 토큰 체크
		User followerUser = userDetails.getUser();

		if (followerUser == null) {
			throw new IllegalArgumentException("로그인을 해주세요");
		}

		// 팔로우 할 유저 조회
		User followingUser = userRepository.findById(followingId)
				.orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

		// 본인 팔로우 불가처리
		if (followerUser.getId().equals(followingUser.getId())) {
			throw new IllegalArgumentException("본인을 팔로우 할 수 없습니다.");
		}

		// 중복 팔로우 예외 발생
		// followRepository 에서 두 개의 Id 값이 존재하는지 확인
		if (followRepository.findByFollowerUserAndFollowingUser(followerUser, followingUser).isPresent()) {
			throw new IllegalArgumentException("팔로우가 중복되었습니다.");
		}

		followRepository.save(new Follow(followingUser, followerUser));

		return ResponseEntity.ok().body(new ApiException("팔로우 성공. 유저이름 : " + followingUser.getUsername(), HttpStatus.OK.value()));
	}


	@Transactional
	public ResponseEntity<ApiException> unFollowing(UserDetailsImpl userDetails, Long followingId) {
		// 토큰 체크
		User followerUser = userDetails.getUser();

		if (followerUser == null) {
			throw new IllegalArgumentException("로그인을 해주세요");
		}

		// 언팔로우 할 유저 조회
		User followingUser = userRepository.findById(followingId)
				.orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

		Follow follow = followRepository.findByFollowerUserAndFollowingUser(followerUser, followingUser)
				.orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 아닙니다"));

		followRepository.delete(follow);

		return ResponseEntity.ok().body(new ApiException("팔로우 취소 성공. 취소한 유저이름 : " + followingUser.getUsername(), HttpStatus.OK.value()));
	}

}
