package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.dto.PostResponseDto;
import com.sparta.sprintbackofficeproject.security.UserDetailsImpl;
import com.sparta.sprintbackofficeproject.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController //뷰 만들기 전 임시
public class HomeController {

    private final PostService postservice;

    public HomeController(PostService postservice) {
        this.postservice = postservice;
    }

    //전체 게시글 조회
    @GetMapping("/")
    public List<PostResponseDto> Home(){
            return postservice.getPosts(1L);
    }

}
