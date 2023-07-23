package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.MostUsedHashTagsStatistics;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MostUsedHashTagsStatisticsDto {

    private List<String> hashTags;
    private List<Long> counts;

    public MostUsedHashTagsStatisticsDto(MostUsedHashTagsStatistics statistics) {
        this.hashTags = statistics.getHashTags();
        this.counts = statistics.getCounts();
    }
}