package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
	LikeComment findByComment_IdAndUser_Id(Long commentId, Long userId);
}
