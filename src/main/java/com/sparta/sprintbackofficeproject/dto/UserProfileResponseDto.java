package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.exception.ApiException;
import lombok.Getter;

@Getter

public class UserProfileResponseDto extends ApiException {

    Long id;
    String username;
    String email;
    String imageUrl;
    String introduction;

    public UserProfileResponseDto(Long id, String username, String email, String imageUrl, String introduction) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
        this.introduction = introduction;
    }
}
