package com.sparta.sprintbackofficeproject.controller;

import com.sparta.sprintbackofficeproject.entity.Notice;
import com.sparta.sprintbackofficeproject.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notices")
public class NoticeController {
    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Notice> createNotice(@RequestBody Notice notice) {
        return ResponseEntity.ok(noticeService.createNotice(notice));
    }

    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNotice(@PathVariable Long id) {
        Notice notice = noticeService.getNotice(id);
        return notice != null ? ResponseEntity.ok(notice) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNotice(@PathVariable Long id, @RequestBody Notice notice) {
        return ResponseEntity.ok(noticeService.updateNotice(id, notice));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }
}