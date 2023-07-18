package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.LikePost;
import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {
    private Long id;
    private String content;
    private String imageUrl;
    private int views;
    private LocalDateTime createdAt;
    private User user;
    private List<LikePost> likePostList;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.views = post.getViews();
        this.createdAt = post.getCreatedAt();
        this.user = post.getUser();
        for(LikePost likePost: post.getLikePostList()){
            this.likePostList.add(likePost);
        }
    }
}
