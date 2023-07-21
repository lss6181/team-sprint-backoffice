package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.MostLikedPostsStatistics;
import com.sparta.sprintbackofficeproject.entity.MostUsedHashTagsStatistics;
import lombok.Getter;

@Getter
public class DailyStatisticsDto {
    private final MostLikedPostsStatisticsDto mostLikedPosts;
    private final MostUsedHashTagsStatisticsDto mostUsedHashTags;

    public DailyStatisticsDto(MostLikedPostsStatistics mostLikedPosts, MostUsedHashTagsStatistics mostUsedHashTags) {
        this.mostLikedPosts = new MostLikedPostsStatisticsDto(mostLikedPosts);
        this.mostUsedHashTags = new MostUsedHashTagsStatisticsDto(mostUsedHashTags);
    }
}
