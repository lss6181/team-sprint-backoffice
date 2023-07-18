package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.dto.PostListResponseDto;
import com.sparta.sprintbackofficeproject.dto.PostResponseDto;
import com.sparta.sprintbackofficeproject.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Postservice {

    private final PostRepository postRepository;

    public Postservice(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostListResponseDto getPosts(Long id) { //합친 후 수정
        List<PostResponseDto> postList = postRepository.findPost1(id).stream().map(PostResponseDto::new).collect(Collectors.toList());

        return new PostListResponseDto(postList);
    }
}
