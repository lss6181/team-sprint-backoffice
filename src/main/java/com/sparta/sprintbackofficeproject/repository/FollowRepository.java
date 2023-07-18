package com.sparta.sprintbackofficeproject.repository;

import com.sparta.sprintbackofficeproject.entity.Follow;
import com.sparta.sprintbackofficeproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	Optional<Follow> findByFollowerUserAndFollowingUser(User followUser, User followingUser);
}
