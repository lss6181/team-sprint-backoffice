package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.MostUsedHashTagsStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MostUsedHashTagsStatisticsRepository extends JpaRepository<MostUsedHashTagsStatistics, Long> {
    MostUsedHashTagsStatistics findTopByOrderByIdDesc();

    void deleteAllByGeneratedAtBefore(LocalDateTime time);
}