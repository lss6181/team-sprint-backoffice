package com.sparta.sprintbackofficeproject.service;

import com.sparta.sprintbackofficeproject.dto.DailyStatisticsDto;
import com.sparta.sprintbackofficeproject.dto.NoticeDto;
import com.sparta.sprintbackofficeproject.dto.PostRequestDto;
import com.sparta.sprintbackofficeproject.entity.*;
import com.sparta.sprintbackofficeproject.repository.*;
import com.sparta.sprintbackofficeproject.util.EmailAuth;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;
    private final NoticeRepository noticeRepository;
    private final ReportRepository reportRepository;
    private final MostLikedPostsStatisticsRepository mostLikedPostsStatisticsRepository;
    private final MostUsedHashTagsStatisticsRepository mostUsedHashTagsStatisticsRepository;
    private final EmailAuth emailAuth;

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

    @Scheduled(cron = "0 0 0 * * ?")
    public void generateStatistics() {
        LocalDateTime startOfDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

        List<Post> top5MostLikedPosts = postRepository.findTop5ByCreatedAtAfterOrderByLikePostListSizeDesc(startOfDay, PageRequest.of(0, 5));

        List<MostLikedPost> mostLikedPosts = new ArrayList<>();
        for (Post post : top5MostLikedPosts) {
            MostLikedPost mostLikedPost = new MostLikedPost();
            mostLikedPost.setPostId(post.getId());
            mostLikedPost.setContent(post.getContent());
            mostLikedPost.setLikeCount((long) post.getLikePostList().size());
            mostLikedPosts.add(mostLikedPost);
        }
        MostLikedPostsStatistics mostLikedPostsStatistics = new MostLikedPostsStatistics();
        mostLikedPostsStatistics.setMostLikedPosts(mostLikedPosts);
        mostLikedPostsStatisticsRepository.save(mostLikedPostsStatistics);

        List<Object[]> top5MostUsedHashTags = hashTagRepository.findTop5MostUsedHashTags(PageRequest.of(0, 5));
        List<MostUsedHashTag> mostUsedHashTags = new ArrayList<>();
        for (Object[] object : top5MostUsedHashTags) {
            MostUsedHashTag mostUsedHashTag = new MostUsedHashTag();
            mostUsedHashTag.setHashTag((String) object[0]);
            mostUsedHashTag.setCount(((Number) object[1]).longValue());
            mostUsedHashTags.add(mostUsedHashTag);
        }
        MostUsedHashTagsStatistics mostUsedHashTagsStatistics = new MostUsedHashTagsStatistics();
        mostUsedHashTagsStatistics.setMostUsedHashTags(mostUsedHashTags);
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

        // 게시물에 대한 모든 신고를 먼저 삭제합니다.
        List<Report> reports = reportRepository.findAllByPostId(reportedPost.getId());
        reportRepository.deleteAll(reports);

        postRepository.delete(reportedPost); // 게시물 삭제

        String title = "신고 처리 결과";
        String emailContent = report.getReporter().getUsername() + "님께서"  // getUsername()으로 수정했습니다.
                + "<p>신고한 게시물에 대한 조치가 완료되었습니다. 해당 게시물은 삭제 되었습니다.</p>";

        emailAuth.sendReportResultEmail(report.getReporter().getEmail(), title, emailContent);  // 이메일 보내기
    }
}