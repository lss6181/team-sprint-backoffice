package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.dto.CommentRequestDto;
import com.sparta.sprintbackofficeproject.dto.CommentResponseDto;
import com.sparta.sprintbackofficeproject.exception.ApiException;
import com.sparta.sprintbackofficeproject.security.UserDetailsImpl;
import com.sparta.sprintbackofficeproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    // 댓글 작성
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto, @PathVariable Long postId) {
        CommentResponseDto result = commentService.createComment(requestDto, userDetails.getUser(), postId);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiException> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto) {
        try {
            CommentResponseDto result = commentService.updateComment(commentId, requestDto, userDetails.getUser());
            return ResponseEntity.ok().body(result);
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiException("작성자만 수정 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiException> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiException("댓글 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiException("작성자만 삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }


}
