package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select p.id, u.username, p.created_at, p.content, p.image_url, p.views from post p\n" +
            "    left join like_post lp on p.id = lp.post_id\n" +
            "    left join user u on p.user_id = u.id\n" +
            "    left join follow f on u.id = f.followee_id\n" +
            "    left join follow f2 on lp.user_id = f2.followee_id\n" +
            "    where (p.user_id = ?1)\n" +
            "    or (lp.user_id = ?1)\n" +
            "    or(f.follower_id = ?1)\n" +
            "    or((f2.follower_id = ?1) and (lp.user_id = f2.followee_id))\n" +
            "    order by p.created_at desc", nativeQuery = true)
    List<Post> findPost1(Long id);
}
