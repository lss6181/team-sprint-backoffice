package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponseDto extends ApiResponseDto{

    private Long Id;
    private String UserName;
    private String content;
    private String imageUrl;
    private Integer views;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
//    private List<CommentResponseDto> comments;

    public PostResponseDto(Post post) {
        this.Id = post.getId();
        this.UserName = post.getUser().getUsername();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.views = post.getViews();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
//        if (!(post.getComments() == null)) {
//            this.comments = post.getComments().stream()
//                    .map(CommentResponseDto::new)
//                    .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed()) // 작성날짜 내림차순 - reversed,
//                    // getCreatedAt - 작성일자, comparing - 비교 연산자, sorted - 정렬
//                    .toList();
//        }

    }
}
