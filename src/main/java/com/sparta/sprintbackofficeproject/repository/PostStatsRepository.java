package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.PostStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostStatsRepository extends JpaRepository<PostStats, Long> {
}