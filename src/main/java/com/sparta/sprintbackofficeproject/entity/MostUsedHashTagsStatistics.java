package com.sparta.sprintbackofficeproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class MostUsedHashTagsStatistics extends Statistics {

    @ElementCollection
    private List<String> hashTags;

    @ElementCollection
    private List<Long> counts;

    public MostUsedHashTagsStatistics(List<String> hashTags, List<Long> counts) {
        super();
        this.hashTags = hashTags;
        this.counts = counts;
    }
}

