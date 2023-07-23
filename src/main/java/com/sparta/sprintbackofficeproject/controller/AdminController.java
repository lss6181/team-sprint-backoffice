package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.dto.DailyStatisticsDto;
import com.sparta.sprintbackofficeproject.dto.NoticeDto;
import com.sparta.sprintbackofficeproject.dto.PostRequestDto;
import com.sparta.sprintbackofficeproject.entity.Report;
import com.sparta.sprintbackofficeproject.service.AdminService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 공지 사항 작성, 수정, 삭제 기능
    @PostMapping("/notice")
    public void createNotice(@RequestBody NoticeDto noticeDto) {
        adminService.createNotice(noticeDto);
    }

    @PutMapping("/notice/{id}")
    public void updateNotice(@PathVariable Long id, @RequestBody NoticeDto noticeDto) {
        adminService.updateNotice(id, noticeDto);
    }

    @DeleteMapping("/notice/{id}")
    public void deleteNotice(@PathVariable Long id) {
        adminService.deleteNotice(id);
    }

    // 게시글 관리 (수정 / 삭제) 기능
    @PutMapping("/posts/{id}")
    public void updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
        adminService.updatePost(id, postRequestDto);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id) {
        adminService.deletePost(id);
    }

    // 관리자 통계 확인
    @GetMapping("/daily-stats")
    public ResponseEntity<DailyStatisticsDto> getDailyStatistics() {
        return ResponseEntity.ok(adminService.getDailyStatistics());
    }

    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = adminService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/reports/{reportId}")
    public ResponseEntity<Void> processReport(@PathVariable Long reportId) throws MessagingException {
        adminService.processReport(reportId);
        return ResponseEntity.ok().build();
    }
}
