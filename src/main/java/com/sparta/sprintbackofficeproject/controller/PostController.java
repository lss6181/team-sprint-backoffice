package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.dto.ApiResponseDto;
import com.sparta.sprintbackofficeproject.dto.PostRequestDto;
import com.sparta.sprintbackofficeproject.dto.PostResponseDto;
import com.sparta.sprintbackofficeproject.security.UserDetailsImpl;
import com.sparta.sprintbackofficeproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    // 특정 게시글 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId) {
        PostResponseDto result = postService.getPostById(postId);

        return ResponseEntity.ok().body(result);
    }

    // 게시글 작성
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostRequestDto requestDto) {
        PostResponseDto result = postService.createPost(requestDto, userDetails.getUser());

        return ResponseEntity.status(201).body(result);
    }

    // 게시글 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponseDto> updatePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId, @RequestBody PostRequestDto requestDto) {
        try {
            PostResponseDto result = postService.updatePost(postId, requestDto, userDetails.getUser());
            return ResponseEntity.ok().body(result);
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 수정 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponseDto> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        try {
            postService.deletePost(postId, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("게시글 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 게시글에 user 태그
    @PostMapping("/posts/{postId}/tag-user")
    public ResponseEntity<ApiResponseDto> tagUserByUserName(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId, @RequestParam String userName) {
        try {
            PostResponseDto result = postService.tagUserByUserName(userDetails.getUser(),postId, userName);
            return ResponseEntity.ok().body(result);
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 태그 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 게시글 태그 취소
    @DeleteMapping("/posts/{tagUserInPostId}")
    private ResponseEntity<ApiResponseDto> deleteTagUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long tagUserInPostId) {
        try {
            postService.deleteTagUser(userDetails.getUser(), tagUserInPostId);
            return ResponseEntity.ok().body(new ApiResponseDto("태그 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }
}
