package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBy();

    @Query(value = "select p.id, p.created_at, p.content, p.image_url, p.views from post p " +
            "left join like_post lp on p.id = lp.post_id " +
            "left join user u on p.user_id = u.id " +
            "left join follow f on u.id = f.followee_id " +
            "left join follow f2 on lp.user_id = f2.followee_id " +
            "where (p.user_id = ?1) " +
            "or (lp.user_id = ?1) " +
            "or(f.follower_id = ?1) " +
            "or((f2.follower_id = ?1) " +
            "and (lp.user_id = f2.followee_id)) " +
            "order by p.created_at desc", nativeQuery = true)
    List<Post> findPost1(Long id);


}
