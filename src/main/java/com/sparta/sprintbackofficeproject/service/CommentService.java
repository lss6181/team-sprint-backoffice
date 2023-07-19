package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.dto.CommentRequestDto;
import com.sparta.sprintbackofficeproject.dto.CommentResponseDto;
import com.sparta.sprintbackofficeproject.entity.Comment;
import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.entity.User;
import com.sparta.sprintbackofficeproject.entity.UserRoleEnum;
import com.sparta.sprintbackofficeproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostService postService;
    private final CommentRepository commentRepository;

    //  댓글 작성
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user, Long postId) {
        Post post = postService.findPost(postId);
        Comment comment = new Comment(post, requestDto, user);
        Comment saveComment = commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);
        return commentResponseDto;
//        comment.setUser(user);
//        comment.setPost(post);
//
//        var savedComment = commentRepository.save(comment);
//
//        return new CommentResponseDto(savedComment);
    }


    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(commentId);

        // 요청자가 운영자 이거나 댓글 작성자(post.user) 와 요청자(user) 가 같은지 체크
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || comment.getUser().getUsername().equals(user.getUsername()))) {
            throw new RejectedExecutionException();
        }

        comment.setContent(requestDto.getContent());

        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, User user) {
       Comment comment = findComment(commentId);

        // 요청자가 운영자 이거나 댓글 작성자(post.user) 와 요청자(user) 가 같은지 체크
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || comment.getUser().getUsername().equals(user.getUsername()))) {
            throw new RejectedExecutionException();
        }

        commentRepository.delete(comment);
    }

    // 댓글 찾기
    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.")
        );
    }
}
