package com.sparta.sprintbackofficeproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "post_stats")
public class PostStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "likes_count")
    private Integer likesCount;

    @Column(name = "date")
    private LocalDateTime date;

    public PostStats(Post post, Integer viewCount, Integer likesCount) {
        this.post = post;
        this.viewCount = viewCount;
        this.likesCount = likesCount;
        this.date = LocalDateTime.now();
    }
}
