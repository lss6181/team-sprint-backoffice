package com.sparta.sprintbackofficeproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "likes_comment")
public class LikeComment extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "like_comment_id")
	private Long id;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "comment_id")
//	private Comment comment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

//	public LikeComment(Comment comment, User user) {
//		this.comment = comment;
//		this.user = user;
//	}
}
