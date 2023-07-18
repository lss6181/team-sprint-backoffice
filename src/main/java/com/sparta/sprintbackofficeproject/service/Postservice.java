package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.dto.PostResponseDto;
import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Postservice {

    private final PostRepository postRepository;

    public Postservice(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostResponseDto> getPosts(Long id) { //합친 후 수정
        List<Post> postList = postRepository.findAllBy();
        List<PostResponseDto> responseDtoList = new ArrayList<>();

        for(Post post : postList){
            responseDtoList.add(new PostResponseDto(post));
        }

        return responseDtoList;
    }
}
