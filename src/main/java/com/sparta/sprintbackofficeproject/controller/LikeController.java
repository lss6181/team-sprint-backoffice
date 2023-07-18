package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.exception.ApiException;
import com.sparta.sprintbackofficeproject.security.UserDetailsImpl;
import com.sparta.sprintbackofficeproject.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {
	private final LikeService likeService;

	// 게시글 좋아요
	@PostMapping("/posts/{postId}")
	public ResponseEntity<ApiException> onClickLikePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {

		likeService.onClickLikePost(userDetails, postId);

		return ResponseEntity.ok().body(new ApiException("좋아요 성공",HttpStatus.OK.value()));
	}

	// 게시글 좋아요 취소
	@DeleteMapping("/posts/{likePostId}")
	public ResponseEntity<ApiException> deleteLikePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long likePostId) {

		likeService.deleteLikePost(userDetails,likePostId);

		return ResponseEntity.ok().body(new ApiException("좋아요 취소 성공", HttpStatus.OK.value()));
	}


	// 댓글 좋아요
	@PostMapping("/comments/{commentId}")
	public ResponseEntity<ApiException> onClickLikeComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId) {

		likeService.onClickLikeComment(userDetails, commentId);

		return ResponseEntity.ok().body(new ApiException("좋아요 성공", HttpStatus.OK.value()));
	}

	// 댓글 좋아요 취소
	@DeleteMapping("/comments/{likeCommentId}")
	public ResponseEntity<ApiException> deleteLikeComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long likeCommentId) {

		likeService.deleteLikeComment(userDetails,likeCommentId);

		return ResponseEntity.ok().body(new ApiException("좋아요 취소 성공", HttpStatus.OK.value()));
	}
}
