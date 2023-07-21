package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.HashTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
	HashTag findByPost_IdAndHashTag(Long postId, String hashTag);
	@Query("SELECT h.hashTag, COUNT(h) FROM HashTag h GROUP BY h.hashTag ORDER BY COUNT(h) DESC")
	List<Object[]> findTop5MostUsedHashTags(Pageable pageable);
}
