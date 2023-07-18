package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.TagUserInPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagUserInPostRepository extends JpaRepository<TagUserInPost, Long> {

	TagUserInPost findByUser_IdAndPost_Id(Long userId, Long postId);
}
