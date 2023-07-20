package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.Follow;
import com.sparta.sprintbackofficeproject.exception.ApiException;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter

public class UserProfileResponseDto extends ApiException {

    Long id;
    String username;
    String email;
    String imageUrl;
    String introduction;
    private int totalFollowings; // 팔로잉 수
    private List<String> followings; // 내가 팔로우 하고있는 사람들 (나의 팔로잉)
    private int totalFollowers; // 팔로워 수
    private List<String> followers; // 나를 팔로잉 하고있는 사람들(나의 팔로워)

    public UserProfileResponseDto(Long id, String username, String email, String imageUrl, String introduction, List<String> following, List<String> follower) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
        this.introduction = introduction;
        this.followings = following;
        this.totalFollowings = following.size();
        this.followers = follower;
        this.totalFollowers = follower.size();
    }
}
