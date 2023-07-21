package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.dto.DailyStatisticsDto;
import com.sparta.sprintbackofficeproject.dto.NoticeDto;
import com.sparta.sprintbackofficeproject.dto.PostRequestDto;
import com.sparta.sprintbackofficeproject.dto.ReportRequestDto;
import com.sparta.sprintbackofficeproject.entity.*;
import com.sparta.sprintbackofficeproject.repository.*;
import com.sparta.sprintbackofficeproject.util.EmailAuth;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private HashTagRepository hashTagRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private MostLikedPostsStatisticsRepository mostLikedPostsStatisticsRepository;

    @Autowired
    private MostUsedHashTagsStatisticsRepository mostUsedHashTagsStatisticsRepository;

    @Autowired
    private EmailAuth emailAuth;

    // 공지사항 관련 기능
    public void createNotice(NoticeDto noticeDto) {
        Notice notice = new Notice();
        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());
        noticeRepository.save(notice);
    }

    public void updateNotice(Long id, NoticeDto noticeDto) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 없습니다."));
        notice.setTitle(noticeDto.getTitle());
        notice.setContent(noticeDto.getContent());
        noticeRepository.save(notice);
    }

    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    // 관리자 게시물 (수정 / 삭제) 관리 기능

    public void updatePost(Long id, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 없습니다."));
        post.setContent(postRequestDto.getContent());
        post.setImageUrl(postRequestDto.getImageUrl());
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // 매일 자정 좋아요 / 태그 가장 많은 게시글 top5 db 저장 스케쥴링 기능
    public void generateStatistics() {
        LocalDateTime startOfDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

        List<Post> top5MostLikedPosts = postRepository.findTop5ByCreatedAtAfterOrderByLikePostListSizeDesc(startOfDay);
        List<Long> postIds = new ArrayList<>();
        List<String> postContents = new ArrayList<>();
        List<Long> likeCounts = new ArrayList<>();
        for (Post post : top5MostLikedPosts) {
            postIds.add(post.getId());
            postContents.add(post.getContent());
            likeCounts.add((long) post.getLikePostList().size());
        }
        MostLikedPostsStatistics mostLikedPostsStatistics = new MostLikedPostsStatistics(postIds, postContents, likeCounts);
        mostLikedPostsStatisticsRepository.save(mostLikedPostsStatistics);

        List<Object[]> top5MostUsedHashTags = hashTagRepository.findTop5MostUsedHashTags(PageRequest.of(0, 5));
        List<String> hashTags = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        for (Object[] object : top5MostUsedHashTags) {
            hashTags.add((String) object[0]);
            counts.add(((Number) object[1]).longValue());
        }
        MostUsedHashTagsStatistics mostUsedHashTagsStatistics = new MostUsedHashTagsStatistics(hashTags, counts);
        mostUsedHashTagsStatisticsRepository.save(mostUsedHashTagsStatistics);
    }

    // 일요일 자정마다 db에 쌓인 통계 데이터를 삭제
    @Scheduled(cron = "0 0 0 ? * SUN")
    public void deleteWeeklyStatistics() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        mostLikedPostsStatisticsRepository.deleteAllByGeneratedAtBefore(oneWeekAgo);
        mostUsedHashTagsStatisticsRepository.deleteAllByGeneratedAtBefore(oneWeekAgo);
    }

    // 통계 확인

    public DailyStatisticsDto getDailyStatistics() {
        MostLikedPostsStatistics mostLikedPostsStatistics = mostLikedPostsStatisticsRepository.findTopByOrderByIdDesc();
        MostUsedHashTagsStatistics mostUsedHashTagsStatistics = mostUsedHashTagsStatisticsRepository.findTopByOrderByIdDesc();

        return new DailyStatisticsDto(mostLikedPostsStatistics, mostUsedHashTagsStatistics);
    }

    // 사용자 신고 관련 기능

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Transactional
    public void processReport(Long reportId) throws MessagingException {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("해당 신고가 존재하지 않습니다."));

        Post reportedPost = report.getPost();
        postRepository.delete(reportedPost); // 게시물 삭제

        String title = "신고 처리 결과";
        String emailContent = "<h1>안녕하세요, " + report.getReporter().getUsername() + "님.</h1>"  // getUsername()으로 수정했습니다.
                + "<p>신고한 게시물에 대한 조치가 완료되었습니다. 해당 게시물은 삭제 되었습니다.</p>";

        emailAuth.sendReportResultEmail(report.getReporter().getEmail(), title, emailContent);  // 이메일 보내기

        reportRepository.delete(report); // 신고 처리 후 해당 신고 내역 삭제
    }
}