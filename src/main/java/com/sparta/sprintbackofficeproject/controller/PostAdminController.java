package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/posts")
@PreAuthorize("hasRole('ADMIN')")
public class PostAdminController {

    private final PostService postService;

    @Autowired
    public PostAdminController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody Post updatedPost) {
        Post post = postService.getPostById(postId);
        if (post != null) {
            post.setContent(updatedPost.getContent());
            return ResponseEntity.ok(postService.updatePost(post));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        if (postService.deletePost(postId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
