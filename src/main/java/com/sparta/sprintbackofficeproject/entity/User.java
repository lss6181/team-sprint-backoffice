package com.sparta.sprintbackofficeproject.entity;

import com.sparta.sprintbackofficeproject.dto.ModifyRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String imageUrl;

    @Column
    private String introduction;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Follow> followsMeList = new ArrayList<>(); // '나를 팔로우'하는 사람들 (나를 팔로잉 하는 필드)

    @OneToMany(mappedBy = "followerUser", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Follow> iFollowList = new ArrayList<>(); // '내가 팔로우'하는 사람들 (=내가 팔로워인 필드)

    @Builder
    public User(String username, String password, String email, String imageUrl, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User update(String username, String imageUrl) {
        this.username = username;
        this.imageUrl = imageUrl;
        return this;
    }

    // 유저 정보 수정
    public void modifyProfile(ModifyRequestDto modifyRequestDto) {
        this.email = modifyRequestDto.getEmail();
        this.imageUrl = modifyRequestDto.getImageUrl();
        this.introduction = modifyRequestDto.getIntroduction();
        this.password = modifyRequestDto.getPassword();
    }
}