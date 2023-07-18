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
    private int likeCount;  // 좋아요 수 필드

    public PostResponseDto(Post post) {
        this.Id = post.getId();
        this.UserName = post.getUser().getUsername();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.views = post.getViews();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.likeCount = post.getLikePostList().size(); // 좋아요 누른 게시글에 관계설정 한 likePostList.size()로 좋아요 갯수 표현
//        if (!(post.getComments() == null)) {
//            this.comments = post.getComments().stream()
//                    .map(CommentResponseDto::new)
//                    .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed()) // 작성날짜 내림차순 - reversed,
//                    // getCreatedAt - 작성일자, comparing - 비교 연산자, sorted - 정렬
//                    .toList();
//        }

    }
}
