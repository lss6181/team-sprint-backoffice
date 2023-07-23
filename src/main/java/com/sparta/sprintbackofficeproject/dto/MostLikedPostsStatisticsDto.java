package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.MostLikedPostsStatistics;
import com.sparta.sprintbackofficeproject.entity.MostUsedHashTagsStatistics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MostLikedPostsStatisticsDto {

    private List<Long> postIds;
    private List<Long> likeCounts;

    public MostLikedPostsStatisticsDto(MostLikedPostsStatistics mostLikedPostsStatistics) {
    }
}
