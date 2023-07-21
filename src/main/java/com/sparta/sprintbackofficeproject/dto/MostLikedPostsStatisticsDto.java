package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.MostLikedPostsStatistics;
import com.sparta.sprintbackofficeproject.entity.MostUsedHashTagsStatistics;
import lombok.Getter;

import java.util.List;

@Getter
public class MostLikedPostsStatisticsDto {

    private List<Long> postIds;
    private List<Long> likeCounts;

    public MostLikedPostsStatisticsDto(MostLikedPostsStatistics statistics) {
        this.postIds = statistics.getPostIds();
        this.likeCounts = statistics.getLikeCounts();
    }
}
