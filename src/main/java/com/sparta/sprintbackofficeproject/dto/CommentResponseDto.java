package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.Comment;
import com.sparta.sprintbackofficeproject.exception.ApiException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto extends ApiException {
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCount;  // 좋아요 수 필드

    public CommentResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCount = comment.getLikePostList().size();
    }
}
