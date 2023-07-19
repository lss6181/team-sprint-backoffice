package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
	HashTag findByPost_IdAndHashTag(Long postId, String hashTag);
}
