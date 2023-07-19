package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.entity.PostStats;
import com.sparta.sprintbackofficeproject.entity.Tag;
import com.sparta.sprintbackofficeproject.repository.PostRepository;
import com.sparta.sprintbackofficeproject.repository.PostStatsRepository;
import com.sparta.sprintbackofficeproject.repository.TagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PostStatsService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostStatsRepository postStatsRepository;

    public PostStatsService(PostRepository postRepository, TagRepository tagRepository, PostStatsRepository postStatsRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postStatsRepository = postStatsRepository;
    }

    public Long getTotalViews() {
        return postRepository.getTotalViews();
    }

    public List<Post> getMostLikedPosts(int limit) {
        // DB에서 0부터 끝까지 가장 많이 좋아요를 받은 게시글을 확인 후 가져옵니다.
        return postRepository.getMostLikedPosts(PageRequest.of(0, limit));
    }

    public List<Tag> getMostUsedTags(int limit) {
        // DB에서 0부터 끝까지 가장 많이 사용된 태그를 확인 후 가져옵니다.
        return tagRepository.getMostUsedTags(PageRequest.of(0, limit));
    }

    public List<PostStats> getTopPostsByViews() {
        // 모든 게시물의 통계 데이터를 가져옵니다.
        List<PostStats> allStats = postStatsRepository.findAll();
        // 통계 데이터를 조회수에 따라 내림차순으로 정렬합니다.
        allStats.sort((stats1, stats2) -> stats2.getViewCount() - stats1.getViewCount());
        return allStats;
    }

    public List<PostStats> getTopPostsByLikes() {
        // 모든 게시물의 통계 데이터를 가져옵니다.
        List<PostStats> allStats = postStatsRepository.findAll();
        // 통계 데이터를 좋아요 수에 따라 내림차순으로 정렬합니다.
        allStats.sort((stats1, stats2) -> stats2.getLikesCount() - stats1.getLikesCount());
        return allStats;
    }

    @Scheduled(cron = "0 0 * * *")
    public void generateDailyPostStats() {
        List<Post> posts = postRepository.findAll();

        for (Post post : posts) {
            PostStats postStats = new PostStats(post, post.getViewCount(), post.getLikes().size());
            postStatsRepository.save(postStats);
        }
    }

}
