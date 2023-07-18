package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.dto.PostResponseDto;
import com.sparta.sprintbackofficeproject.service.Postservice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController //뷰 만들기 전 임시
public class HomeController {

    private final Postservice postservice;

    public HomeController(Postservice postservice) {
        this.postservice = postservice;
    }

    @GetMapping("/")
    public List<PostResponseDto> Home(){
            return postservice.getPosts(1L);
    }

}
