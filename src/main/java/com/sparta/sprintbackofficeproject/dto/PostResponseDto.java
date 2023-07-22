package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.exception.ApiException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    private int likeCount;  // 좋아요 수 필드
    private List<TagUserResponseDto> tagUsers;    // 태그한 유저들
    private List<HashTagResponseDto> hashTags;  // 해쉬태그 필드

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
        this.likeCount = post.getLikePostList().size(); // 좋아요 누른 게시글에 관계설정 한 likePostList.size()로 좋아요 갯수 표현
        if (!(post.getHashTagList() == null)) {
            this.tagUsers = post.getTagUserInPostList().stream()
                    .map(TagUserResponseDto::new)
                    .collect(Collectors.toList());  // 태그한 유저들 조회
        }
        if (!(post.getHashTagList()==null)) {
			this.hashTags = post.getHashTagList().stream()
					.map(HashTagResponseDto::new)
					.collect(Collectors.toList());  // 해쉬태그
        }
    }
}