package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.exception.ApiException;
import com.sparta.sprintbackofficeproject.security.UserDetailsImpl;
import com.sparta.sprintbackofficeproject.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {
	private final FollowService followService;

	// 팔로우
	@PostMapping("/{followingId}")
	public ResponseEntity<ApiException> following(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long followingId) {
		return followService.following(userDetails, followingId);
	}

	// 언팔로우
	@DeleteMapping("/{followingId}")
	public ResponseEntity<ApiException> unFollowing(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long followingId) {
		return followService.unFollowing(userDetails, followingId);
	}
}
