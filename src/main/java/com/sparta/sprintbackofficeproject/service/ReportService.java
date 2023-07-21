package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.entity.Post;
import com.sparta.sprintbackofficeproject.entity.Report;
import com.sparta.sprintbackofficeproject.entity.User;
import com.sparta.sprintbackofficeproject.repository.PostRepository;
import com.sparta.sprintbackofficeproject.repository.ReportRepository;
import com.sparta.sprintbackofficeproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository, PostRepository postRepository) {
        this.reportRepository = reportRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Report createReport(Long reporterId, Long reportedUserId, Long postId, String reason) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new IllegalArgumentException("신고자가 존재하지 않습니다."));
        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new IllegalArgumentException("신고 당한 사용자가 존재하지 않습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Report report = new Report(null, reporter, reportedUser, post, reason, LocalDateTime.now());

        return reportRepository.save(report);
    }
}