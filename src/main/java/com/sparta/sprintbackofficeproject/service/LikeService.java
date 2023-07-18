package com.sparta.sprintbackofficeproject.service;


import com.sparta.sprintbackofficeproject.entity.*;
import com.sparta.sprintbackofficeproject.repository.LikePostRepository;
import com.sparta.sprintbackofficeproject.repository.PostRepository;
import com.sparta.sprintbackofficeproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
	private final PostRepository postRepository;
	//	private final CommentRepository commentRepository;
	private final LikePostRepository likePostRepository;
//	private final LikeCommentRepository likeCommentRepository;


	// 게시글 좋아요
	@Transactional
	public void onClickLikePost(UserDetailsImpl userDetails, Long postId) {

		// 토큰 체크
		User user = userDetails.getUser();

		if (user == null) {
			throw new IllegalArgumentException("Not Found Token");
		}

		// 좋아요 누른 게시글 find
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new IllegalArgumentException("Not Found Post"));

		// 좋아요 누른 게시글이 로그인 사용자 본인 게시글이면 좋아요 불가능
		if (user.getId().equals(post.getUser().getId())) {
			throw new IllegalArgumentException("본인 게시글에 좋아요 불가능 합니다.");
		}

		// 중복 좋아요 방지
		LikePost likePost = likePostRepository.findByPost_IdAndUser_Id(postId, user.getId());
		if (likePost != null) {
			throw new IllegalArgumentException("좋아요를 이미 누르셨습니다.");
		}


		likePostRepository.save(new LikePost(post, user));
	}


	// 게시글 좋아요 취소
	@Transactional
	public void deleteLikePost(UserDetailsImpl userDetails, Long likePostId) {
		// 토큰 체크
		User user = userDetails.getUser();

		if (user == null) {
			throw new IllegalArgumentException("Not Found Token");
		}

		// likePostId로 누른 좋아요 찾아오기
		LikePost likePost = likePostRepository.findById(likePostId)
				.orElseThrow(() -> new IllegalArgumentException("좋아요를 누르지 않았습니다."));

		// 좋아요 누른 본인이거나 admin일경우만 삭제가능하도록 체크
		if (this.checkValidUser(user, likePost.getUser().getId())) {
			throw new IllegalArgumentException("작성자(본인)만 수정/삭제/취소 할 수 있습니다.");
		}

		likePostRepository.delete(likePost);
	}


//	// 댓글 좋아요
//	@Transactional
//	public void onClickLikeComment(UserDetailsImpl userDetails, Long commentId) {
//		// 토큰 체크
//		User user = userDetails.getUser();
//
//		if (user == null) {
//			throw new IllegalArgumentException("Not Found Token");
//		}
//
//		/**
//		 * Comment CRUD 취합되면 수정반영 예정
//		 */
////		// 좋아요 누른 댓글 find
////		Comment comment = commentRepository.findById(commentId)
////				.orElseThrow(() -> new IllegalArgumentException("Not Found Comment");
////
////		// 좋아요 누른 댓글이 로그인 사용자 본인 게시글이면 좋아요 불가능
////		if (user.getId().equals(comment.getUser().getId())) {
////			throw new IllegalArgumentException("본인 댓글에 좋아요 불가능 합니다.");
////		}
//
//		// 중복 좋아요 방지
//		LikeComment likeComment = likeCommentRepository.findByComment_IdAndUser_Id(commentId, user.getId());
//		if (likeComment != null) {
//			throw new IllegalArgumentException("좋아요를 이미 누르셨습니다.");
//		}
//
//		/**
//		 * Comment CRUD 취합되면 수정반영 예정
//		 */
////		likeCommentRepository.save(new LikeComment(comment, user));
//	}
//
//
//	// 댓글 좋아요 취소
//	@Transactional
//	public void deleteLikeComment(UserDetailsImpl userDetails, Long likeCommentId) {
//		// 토큰 체크
//		User user = userDetails.getUser();
//
//		if (user == null) {
//			throw new IllegalArgumentException("Not Found Token");
//		}
//
//		// likeCommentId로 누른 좋아요 찾아오기
//		LikeComment likeComment = likeCommentRepository.findById(likeCommentId)
//				.orElseThrow(() -> new IllegalArgumentException("좋아요를 누르지 않았습니다."));
//
//		// 좋아요 누른 본인이거나 admin일 경우만 삭제가능하도록 체크
//		if (this.checkValidUser(user, likeComment.getUser().getId())) {
//			throw new IllegalArgumentException("작성자(본인)만 수정/삭제/취소 할 수 있습니다.");
//		}
//
//		likeCommentRepository.delete(likeComment);
//
//	}

	// 삭제 권한여부 검증
	// 좋아요 누른 본인 아니고 관리자계정도 아닐 경우 true.
	private boolean checkValidUser(User user, Long likeId) {
		boolean result = !(user.getId() == likeId)
				&& !(user.getRole().equals(UserRoleEnum.ADMIN));
		return result;
	}
}

