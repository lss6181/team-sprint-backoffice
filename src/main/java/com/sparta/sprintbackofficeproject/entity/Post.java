package com.sparta.sprintbackofficeproject.entity;


import com.sparta.sprintbackofficeproject.dto.PostRequestDto;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
<<<<<<< HEAD
@Table
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String content;
    @Column
    private String imageUrl;
    @Column
    private int views;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToMany(mappedBy = "post")
//    private List<LikePost> likePostList;
=======
@NoArgsConstructor
@Table(name = "post")
public class Post extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;  // postId

    @ManyToOne
    @JoinColumn(name = "User_Id")  // 유저 고유 식별 고유 Id
    private User user;

    @Column
    private String Content;  // 게시글 내용

    @Column
    private String ImageUrl; //게시글 사진

    @Column
    private int Views;  //조회수 -> 특정 게시글 선택하여 볼 경우 조회수 + 1

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<LikePost> likePostList = new ArrayList<>();    // 좋아요 연관관계 설정


    public Post(PostRequestDto requestDto) {
        this.Content = requestDto.getContent();
        this.ImageUrl = requestDto.getImageUrl();
    }

    public void setUser(User user) {
        this.user =  user;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }

    // 특정 게시글 조회시 조회 카운트 증가
    public void setViews() {
        this.Views = Views + 1;
    }
>>>>>>> 200c50e8ac1ff54d703f22c3ad48044492de778e
}
