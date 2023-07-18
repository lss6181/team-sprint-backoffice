package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {
	LikePost findByPost_IdAndUser_Id(Long postId, Long userId);
}
