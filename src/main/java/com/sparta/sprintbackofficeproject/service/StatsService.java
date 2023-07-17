package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.entity.Tag;
import com.sparta.sprintbackofficeproject.repository.PostRepository;
import com.sparta.sprintbackofficeproject.repository.TagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public StatsService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    public Long getTotalViews() {
        return postRepository.getTotalViews();
    }

    public List<Post> getMostLikedPosts(int limit) {
        return postRepository.getMostLikedPosts(PageRequest.of(0, limit));
    }

    public List<Tag> getMostUsedTags(int limit) {
        return tagRepository.getMostUsedTags(PageRequest.of(0, limit));
    }
}
