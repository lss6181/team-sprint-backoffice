package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT SUM(p.viewCount) FROM Post p")
    Long getTotalViews();
    @Query("SELECT p FROM Post p ORDER BY p.likes.size DESC")
    List<Post> getMostLikedPosts(Pageable pageable);
}
