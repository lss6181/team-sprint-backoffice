package com.sparta.sprintbackofficeproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "likes_post")
public class LikePost extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "like_post_id")
	private Long id;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "post_id")
//	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

//	public LikePost(Post post, User user) {
//		this.post = post;
//		this.user = user;
//	}
}
