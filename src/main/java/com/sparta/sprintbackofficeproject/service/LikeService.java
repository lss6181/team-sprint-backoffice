package com.sparta.sprintbackofficeproject.service;


import com.sparta.sprintbackofficeproject.entity.User;
import com.sparta.sprintbackofficeproject.repository.LikeCommentRepository;
import com.sparta.sprintbackofficeproject.repository.LikePostRepository;
import com.sparta.sprintbackofficeproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
//	private final LikePostRepository likePostRepository;
//	private final LikeCommentRepository likeCommentRepository;
//
//	// 게시글 좋아요
//	public PostResponseDto onClickLikePost(UserDetailsImpl userDetails, Long postId) {
//
//		// 토큰 체크
//		User user = userDetails.getUser();
//
//		if (user == null) {
//			throw new IllegalArgumentException("Not Found Token");
//		}
//
//		return null;
//	}
//
//	// 게시글 좋아요 취소
//	public void deleteLikePost(UserDetailsImpl userDetails, Long postId) {
//		// 토큰 체크
//		User user = userDetails.getUser();
//
//		if (user == null) {
//			throw new IllegalArgumentException("Not Found Token");
//		}
//
//	}
//
//	public CommentResponseDto onClickLikeComment(UserDetailsImpl userDetails, Long commentId) {
//		// 토큰 체크
//		User user = userDetails.getUser();
//
//		if (user == null) {
//			throw new IllegalArgumentException("Not Found Token");
//		}
//
//		return null;
//	}
//
//	public void deleteLikeComment(UserDetailsImpl userDetails, Long commentId) {
//		// 토큰 체크
//		User user = userDetails.getUser();
//
//		if (user == null) {
//			throw new IllegalArgumentException("Not Found Token");
//		}
//
//	}
}

