package com.sparta.sprintbackofficeproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class MostLikedPostsStatistics extends Statistics {

    @ElementCollection
    private List<Long> postIds;

    @ElementCollection
    private List<String> postTitles;

    @ElementCollection
    private List<Long> likeCounts;

    public MostLikedPostsStatistics(List<Long> postIds, List<String> postTitles, List<Long> likeCounts) {
        super();
        this.postIds = postIds;
        this.postTitles = postTitles;
        this.likeCounts = likeCounts;
    }
}