package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.entity.Report;
import com.sparta.sprintbackofficeproject.entity.User;
import com.sparta.sprintbackofficeproject.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Report> getReportsByReportedUser(Long userId) {
        User user = new User();
        user.setId(userId);
        return reportRepository.findByReported(user);
    }

    public Report createReport(Report report) {
        return reportRepository.save(report);
    }
}