package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.Report;
import com.sparta.sprintbackofficeproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByReported(User user); // 신고당한 사용자로 신고 목록 검색
}
