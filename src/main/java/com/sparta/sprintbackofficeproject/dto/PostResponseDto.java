package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.exception.ApiException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class PostResponseDto extends ApiException {

    private Long Id;
    private String UserName;
    private String content;
    private String imageUrl;
    private Integer views;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> comments;

    public PostResponseDto(Post post) {
        this.Id = post.getId();
        this.UserName = post.getUser().getUsername();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.views = post.getViews();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        if (!(post.getComment() == null)) {
            this.comments = post.getComment().stream()
                    .map(CommentResponseDto::new)
                    .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed())
                    .toList();
        }

    }
}
