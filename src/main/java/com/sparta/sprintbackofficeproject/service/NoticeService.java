package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.entity.Notice;
import com.sparta.sprintbackofficeproject.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Notice createNotice(Notice notice) {
        return noticeRepository.save(notice);
    }

    public Notice getNotice(Long id) {
        return noticeRepository.findById(id).orElse(null);
    }

    public Notice updateNotice(Long id, Notice updatedNotice) {
        return noticeRepository.findById(id)
                .map(notice -> {
                    notice.setTitle(updatedNotice.getTitle());
                    notice.setContent(updatedNotice.getContent());
                    notice.setUpdatedAt(LocalDateTime.now());
                    return noticeRepository.save(notice);
                })
                .orElseGet(() -> {
                    updatedNotice.setId(id);
                    return noticeRepository.save(updatedNotice);
                });
    }

    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }
}
