package com.sparta.sprintbackofficeproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MostUsedHashTagsStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<MostUsedHashTag> mostUsedHashTags;

    private LocalDateTime generatedAt;

    public List<String> getHashTags() {
        return this.mostUsedHashTags.stream().map(MostUsedHashTag::getHashTag).collect(Collectors.toList());
    }

    public List<Long> getCounts() {
        return this.mostUsedHashTags.stream().map(MostUsedHashTag::getCount).collect(Collectors.toList());
    }
}
