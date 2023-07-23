package com.sparta.sprintbackofficeproject.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.sparta.sprintbackofficeproject.dto.PostRequestDto;
import com.sparta.sprintbackofficeproject.dto.PostResponseDto;
import com.sparta.sprintbackofficeproject.exception.ApiException;
import com.sparta.sprintbackofficeproject.security.UserDetailsImpl;
import com.sparta.sprintbackofficeproject.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AmazonS3 amazonS3;


    //전체 게시글 조회
    @GetMapping("/posts")
    public List<PostResponseDto> getPosts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.getPosts(userDetails.getUser());
    }

    // 특정 게시글 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId) {
        PostResponseDto result = postService.getPostById(postId);

        return ResponseEntity.ok().body(result);
    }

    // 게시글 작성
    @PostMapping(value = "/posts")
    public ResponseEntity<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart(value = "requestDto") @Valid PostRequestDto requestDto, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        PostResponseDto result = postService.createPost(requestDto, userDetails.getUser(), file);

        return ResponseEntity.status(201).body(result);
    }

    // 게시글 수정
    @PutMapping(value = "/posts/{postId}")
    public ResponseEntity<ApiException> updatePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId, @RequestPart(value = "requestDto") @Valid PostRequestDto requestDto, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        try {
            PostResponseDto result = postService.updatePost(postId, requestDto, userDetails.getUser(), file);
            return ResponseEntity.ok().body(result);
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiException("작성자만 수정 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiException> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        try {
            postService.deletePost(postId, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiException("게시글 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiException("작성자만 삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 게시글에 user 태그
    @PostMapping("/posts/tag-user/{postId}")
    public ResponseEntity<ApiException> tagUserByUserName(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId, @RequestParam String userName) {
        try {
            PostResponseDto result = postService.tagUserByUserName(userDetails.getUser(),postId, userName);
            return ResponseEntity.ok().body(result);
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiException("작성자만 태그 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 게시글 user 태그 취소
    @DeleteMapping("/posts/tag-user/{tagUserInPostId}")
    private ResponseEntity<ApiException> deleteTagUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long tagUserInPostId) {
        try {
            postService.deleteTagUser(userDetails.getUser(), tagUserInPostId);
            return ResponseEntity.ok().body(new ApiException("태그 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiException("작성자만 삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 게시글 hash tag
    @PostMapping("/posts/hash-tag/{postId}")
    public ResponseEntity<ApiException> hashTag(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId, @RequestParam String hashTag) {
        try {
            PostResponseDto result = postService.hashTag(userDetails.getUser(), postId, hashTag);
            return ResponseEntity.ok().body(result);
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiException("작성자만 태그 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // hash tag 삭제
    @DeleteMapping("/posts/hash-tag/{hashTagId}")
    public ResponseEntity<ApiException> deleteHashTag(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long hashTagId) {
        try {
            postService.deleteHashTag(userDetails.getUser(), hashTagId);
            return ResponseEntity.ok().body(new ApiException("태그 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiException("작성자만 삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }
}
