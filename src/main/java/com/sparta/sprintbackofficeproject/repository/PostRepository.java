package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "select p.id, p.created_at, p.modified_at, p.content, p.image_url, p.views, u.user_id\n" +
                "    from post p\n" +
                "    left join likes_post lp on p.id = lp.post_id\n" +
                "    left join users u on p.user_id = u.user_id\n" +
                "    left join follows f on u.user_id = f.following_user_id\n" +
                "    left join follows f2 on lp.user_id = f2.following_user_id\n" +
                "    left join tag_user_in_post tuip on p.id = tuip.post_id\n" +
                "    where p.user_id = ?1\n" +
                "    or f.follower_user_id = ?1\n" +
                "    or (lp.user_id = ?1)\n" +
                "    or((f2.follower_user_id = ?1) and (lp.user_id = f2.following_user_id))\n" +
                "    or(tuip.user_id = ?1)\n" +
                "    group by p.id, p.created_at\n" +
                "    order by if(p.created_at in (select p.created_at from post p\n" +
                "                                   left join users u on p.user_id = u.user_id\n" +
                "                                   left join follows f on u.user_id = f.following_user_id\n" +
                "                                   left join tag_user_in_post tuip on p.id = tuip.post_id\n" +
                "                                   where  p.user_id = ?1\n" +
                "                                   or f.follower_user_id = ?1\n" +
                "                                   or tuip.user_id = ?1)," +
                "                p.created_at, (if(max(lp.created_at) > p.created_at, max(lp.created_at), p.created_at))) desc", nativeQuery = true)
    List<Post> findPost1(Long id);
    @Query("SELECT p FROM Post p WHERE p.createdAt > :startOfDay ORDER BY SIZE(p.likePostList) DESC")
    List<Post> findTop5ByCreatedAtAfterOrderByLikePostListSizeDesc(@Param("startOfDay") LocalDateTime startOfDay, Pageable pageable);

}
