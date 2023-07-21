package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.MostLikedPostsStatistics;
import com.sparta.sprintbackofficeproject.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MostLikedPostsStatisticsRepository extends JpaRepository<MostLikedPostsStatistics, Long> {
    MostLikedPostsStatistics findTopByOrderByIdDesc();
    void deleteAllByGeneratedAtBefore(LocalDateTime time);
}