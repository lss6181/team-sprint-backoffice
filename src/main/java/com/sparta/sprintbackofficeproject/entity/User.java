package com.sparta.sprintbackofficeproject.entity;

import com.sparta.sprintbackofficeproject.dto.ModifyRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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