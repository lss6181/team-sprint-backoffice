package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.entity.Tag;
import com.sparta.sprintbackofficeproject.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class StatsController {
    private final StatsService adminService;

    public StatsController(StatsService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/total-views")
    public ResponseEntity<Long> getTotalViews() {
        return ResponseEntity.ok(adminService.getTotalViews());
    }

    @GetMapping("/most-liked-posts")
    public ResponseEntity<List<Post>> getMostLikedPosts(@RequestParam int limit) {
        return ResponseEntity.ok(adminService.getMostLikedPosts(limit));
    }

    @GetMapping("/most-used-tags")
    public ResponseEntity<List<Tag>> getMostUsedTags(@RequestParam int limit) {
        return ResponseEntity.ok(adminService.getMostUsedTags(limit));
    }
}