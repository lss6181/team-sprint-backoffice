package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.dto.ReportRequestDto;
import com.sparta.sprintbackofficeproject.entity.Report;
import com.sparta.sprintbackofficeproject.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/reports")
    public ResponseEntity<Report> createReport(@RequestBody ReportRequestDto request) {
        Report report = reportService.createReport(request.getReporterId(), request.getReportedUserId(), request.getPostId(), request.getReason());
        return ResponseEntity.ok(report);
    }
}