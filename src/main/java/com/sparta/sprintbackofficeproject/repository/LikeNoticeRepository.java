package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.LikeNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeNoticeRepository extends JpaRepository<LikeNotice, Long> {
    int countByNoticeId(Long noticeId);
    void deleteByNoticeIdAndUserId(Long noticeId, Long userId);
    Optional<LikeNotice> findByNoticeIdAndUserId(Long noticeId, Long userId);
}
