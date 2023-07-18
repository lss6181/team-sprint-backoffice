package com.sparta.sprintbackofficeproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "follows")
public class Follow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "follow_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "following_user_id")
	User followingUser;     // 팔로우 할 유저의 id

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "follower_user_id")
	User followerUser;      // 팔로우 하는 유저의 id

	public Follow(User followingUser, User followerUser) {
		this.followingUser = followingUser;
		this.followerUser = followerUser;
	}
}
