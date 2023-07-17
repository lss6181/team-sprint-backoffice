package com.sparta.sprintbackofficeproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter; // 신고하는 사용자

    @ManyToOne
    @JoinColumn(name = "reported_id")
    private User reported; // 신고당하는 사용자

    @Column(length = 500)
    private String reason; // 신고 이유
}