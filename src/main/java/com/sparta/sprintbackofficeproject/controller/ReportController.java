package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.entity.Report;
import com.sparta.sprintbackofficeproject.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reported/{userId}")
    public ResponseEntity<List<Report>> getReportsByReportedUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getReportsByReportedUser(userId));
    }

    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(reportService.createReport(report));
    }
}
