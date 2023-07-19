package com.sparta.sprintbackofficeproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tag_user_in_post")
@NoArgsConstructor
public class TagUserInPost {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;  // 태그한 유저 정보 담기위한 FK

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;  // 태그가 위치한 게시글

	public TagUserInPost(User user, Post post) {
		this.user = user;
		this.post = post;
	}
}
