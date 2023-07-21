package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.dto.PostRequestDto;
import com.sparta.sprintbackofficeproject.dto.PostResponseDto;
import com.sparta.sprintbackofficeproject.entity.*;
import com.sparta.sprintbackofficeproject.repository.HashTagRepository;
import com.sparta.sprintbackofficeproject.repository.PostRepository;
import com.sparta.sprintbackofficeproject.repository.TagUserInPostRepository;
import com.sparta.sprintbackofficeproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagUserInPostRepository tagUserInPostRepository;
    private final HashTagRepository hashTagRepository;
    private final FileUploadService fileUploadService;
    private String ImageUrl;

    @Value("${cloud.aws.s3.bucket}")
    private String buketName;

    //전체 게시글 조회하기
    public List<PostResponseDto> getPosts(User user) {
        List<Post> postList = postRepository.findPost1(user.getId());
        List<PostResponseDto> responseDtoList = new ArrayList<>();

        for (Post post : postList) {
            responseDtoList.add(new PostResponseDto(post));
        }

        return responseDtoList;
    }

    @Transactional
    // 특정 게시글 조회
    public PostResponseDto getPostById(Long postId) {

        Post post = findPost(postId);
        post.setViews(); // 조회 카운트 + 1
        System.out.println(post.getImageUrl());


        return new PostResponseDto(post);
    }

    // 게시글 작성
    public PostResponseDto createPost(PostRequestDto requestDto, User user, MultipartFile file) throws IOException {
        ImageUrl = fileUploadService.uploadFile(file);  // 파일 업로드
        requestDto.setImageUrl(ImageUrl);
        Post post = new Post(requestDto);
        post.setUser(user);
        post.setBuketName(buketName);

        postRepository.save(post);

        return new PostResponseDto(post);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, User user, MultipartFile file) throws IOException {
        Post post = findPost(postId);

        // 게시글 작성자(post.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().getId().equals(user.getId()))) {
            throw new RejectedExecutionException();
        }

        System.out.println("imageUrl의 값은 ? : " + post.getImageUrl());
        System.out.println("file 의 값은 ? : " + file);
        if (file != null) {  // 게시글의 수정 내용중 사진 수정 존재이 있을경우
            if (post.getImageUrl() != null && post.getImageUrl() != "") { // 기존 게시글의 사진이 존재할 경우
                fileUploadService.deleteFile(post.getImageUrl()); //사진 삭제
            }
        }
        ImageUrl = fileUploadService.uploadFile(file); // 파일 업로드


        requestDto.setImageUrl(ImageUrl);
        post.setContent(requestDto.getContent());
        post.setImageUrl(requestDto.getImageUrl());

        postRepository.save(post);

        return new PostResponseDto(post);
    }

    // 게시글 삭제
    public void deletePost(Long postId, User user) {
        Post post = findPost(postId);

        // 게시글 작성자(post.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().getId().equals(user.getId()))) {
            throw new RejectedExecutionException();
        }

        postRepository.delete(post);
    }

    // 게시글에 user 태그
    public PostResponseDto tagUserByUserName(User user, Long postId, String userName) {
        // 태그 입력한 userName 가져오기
        User tagUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));

        // 본인을 태그하는 건 불가하도록 예외처리
        if (user.getId().equals(tagUser.getId())) {
            throw new RejectedExecutionException("본인 태그 불가!");
        }

        // 태그한 게시글 find
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 중복 태그 방지
        TagUserInPost tagUserInPost = tagUserInPostRepository.findByUser_IdAndPost_Id(tagUser.getId(), post.getId());
        if (tagUserInPost != null) {
            throw new IllegalArgumentException("태그 중복 불가!");
        }


        tagUserInPostRepository.save(new TagUserInPost(tagUser, post));

        return new PostResponseDto(post);
    }

    // user 태그 삭제
    public void deleteTagUser(User user, Long tagUserInPostId) {
        // 삭제할 태그 find
        TagUserInPost tagUser = tagUserInPostRepository.findById(tagUserInPostId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 태그 입니다."));

        // 태그 한 자와 현재 로그인 사용자가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || tagUser.getPost().getUser().getId().equals(user.getId()))) {
            throw new RejectedExecutionException();
        }

        tagUserInPostRepository.delete(tagUser);
    }

    // 게시글 해쉬태그
    public PostResponseDto hashTag(User user, Long postId, String hashTag) {
        //태그한 게시글 find
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 중복 태그 방지
        HashTag overlapHashTag = hashTagRepository.findByPost_IdAndHashTag(postId, hashTag);
        if (overlapHashTag != null) {
            throw new IllegalArgumentException("이미 태그한 내용 입니다.");
        }

        hashTagRepository.save(new HashTag(hashTag, post));

        return new PostResponseDto(post);
    }

    // 해쉬태그 삭제
    public void deleteHashTag(User user, Long hashTagId) {
        // 삭제할 태그 find
        HashTag hashTag = hashTagRepository.findById(hashTagId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그 입니다."));

        // 태그 한 자와 현재 로그인 사용자가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || hashTag.getPost().getUser().getId().equals(user.getId()))) {
            throw new RejectedExecutionException();
        }

        hashTagRepository.delete(hashTag);
    }


    // 게시글 id로 게시글 찾기
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }
}