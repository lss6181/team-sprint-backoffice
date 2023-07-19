package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBy();

    @Query(value = "select p.id, p.created_at, p.modified_at, p.content, p.image_url, p.views, u.user_id\n" +
            "            from post p\n" +
            "            left join likes_post lp on p.id = lp.post_id\n" +
            "            left join users u on p.user_id = u.user_id\n" +
            "            left join follows f on u.user_id = f.following_user_id\n" +
            "            left join follows f2 on lp.user_id = f2.following_user_id\n" +
            "            left join tag_user_in_post tuip on p.id = tuip.post_id\n" +
            "            where (lp.created_at in (select min(lp.created_at)\n" +
            "                                    from post p left join likes_post lp\n" +
            "                                    on p.id = lp.post_id\n" +
            "                                    where (p.user_id = ?1)))\n" +
            "            or ((p.user_id = ?1) and (lp.created_at is null))\n" +
            "            or (lp.user_id = ?1)\n" +
            "            or (lp.created_at in (select min(lp.created_at)\n" +
            "                                  from  post p\n" +
            "                                    left join follows f on p.user_id = f.following_user_id\n" +
            "                                    left join likes_post lp on p.id = lp.post_id\n" +
            "                                  where (f.follower_user_id = ?1)))\n" +
            "            or((f.follower_user_id = ?1)and (lp.created_at is null))\n" +
            "            or((f2.follower_user_id = ?1) and (lp.user_id = f2.following_user_id))\n" +
            "            or(tuip.user_id = ?1)\n" +
            "            order by (if(p.created_at < lp.created_at, lp.created_at, p.created_at)) desc", nativeQuery = true)
    List<Post> findPost1(Long id);
}
