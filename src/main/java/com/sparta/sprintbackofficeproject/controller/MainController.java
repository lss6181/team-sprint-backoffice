package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.dto.PostRequestDto;
import com.sparta.sprintbackofficeproject.dto.PostResponseDto;
import com.sparta.sprintbackofficeproject.service.Postservice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController //뷰 만들기 전 임시
public class MainController {

    @GetMapping("/")
    public List<PostResponseDto> Main(){
//        if(){ //유저정보가 없을 경우
//
//        }else{// 유저정보가 있을 경우
            return Postservice.getPosts();
//        }
    }

}
