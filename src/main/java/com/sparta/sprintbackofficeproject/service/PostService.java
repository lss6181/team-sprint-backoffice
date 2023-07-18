package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.dto.PostRequestDto;
import com.sparta.sprintbackofficeproject.dto.PostResponseDto;
import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.entity.TagUserInPost;
import com.sparta.sprintbackofficeproject.entity.User;
import com.sparta.sprintbackofficeproject.entity.UserRoleEnum;
import com.sparta.sprintbackofficeproject.repository.PostRepository;
import com.sparta.sprintbackofficeproject.repository.TagUserInPostRepository;
import com.sparta.sprintbackofficeproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final TagUserInPostRepository tagUserInPostRepository;

    @Transactional
    // 특정 게시글 조회
    public PostResponseDto getPostById(Long postId) {

        Post post = findPost(postId);
        post.setViews(); // 조회 카운트 + 1


        return new PostResponseDto(post);
    }

    // 게시글 작성
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto);
        post.setUser(user);

        postRepository.save(post);

        return new PostResponseDto(post);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, User user) {
        Post post = findPost(postId);

        // 게시글 작성자(post.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
        if  (!(user.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().getUsername().equals(user.getUsername()))) {
            throw new RejectedExecutionException();
        }

        post.setContent(requestDto.getContent());
        post.setImageUrl(requestDto.getImageUrl());

		postRepository.save(post);

        return new PostResponseDto(post);
    }

    // 게시글 삭제
    public void deletePost(Long postId, User user) {
        Post post = findPost(postId);

        // 게시글 작성자(post.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().getUsername().equals(user.getUsername()))) {
            throw new RejectedExecutionException();
        }

        postRepository.delete(post);
    }

	// 게시글에 유저 태그
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

	public void deleteTagUser(User user, Long tagUserInPostId) {
		TagUserInPost tagUser = tagUserInPostRepository.findById(tagUserInPostId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지않는 태그 입니다."));

		// 태그 한 자와 현재 로그인 사용자가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
		if (!(user.getRole().equals(UserRoleEnum.ADMIN) || tagUser.getPost().getUser().equals(user))) {
			throw new RejectedExecutionException();
		}

		tagUserInPostRepository.delete(tagUser);
	}


	// 게시글 id로 게시글 찾기
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }


}
