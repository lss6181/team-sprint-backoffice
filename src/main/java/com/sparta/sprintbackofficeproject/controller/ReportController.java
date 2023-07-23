package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.dto.ReportDto;
import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.entity.Report;
import com.sparta.sprintbackofficeproject.repository.PostRepository;
import com.sparta.sprintbackofficeproject.security.UserDetailsImpl;
import com.sparta.sprintbackofficeproject.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final PostRepository postRepository;

    @PostMapping("/reports/{postId}")
    public ResponseEntity<ReportDto> createReport(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ReportDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 없습니다."));
        Long reportedUserId = post.getUser().getId();  // 게시물 작성자의 ID를 가져옵니다.
        ReportDto report = reportService.createReport(userDetails.getUser().getId(), reportedUserId, postId, request.getReason());
        return ResponseEntity.ok(report);
    }
}