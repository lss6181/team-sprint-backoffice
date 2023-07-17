package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다"));

        // 조회수 증가
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);

        return post;
    }

    public Post updatePost(Post post) {
        if (postRepository.existsById(post.getId())) {
            return postRepository.save(post);
        } else {
            throw new NoSuchElementException("Post not found with id: " + post.getId());
        }
    }

    public boolean deletePost(Long postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return true;
        } else {
            return false;
        }
    }
}
