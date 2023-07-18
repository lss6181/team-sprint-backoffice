package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.TagUserInPost;
import lombok.Getter;

@Getter
public class TagUserResponseDto {
	private Long id;
	private String tagUserName;

	public TagUserResponseDto(TagUserInPost tagUserInPost) {
		this.id = tagUserInPost.getId();
		tagUserName = tagUserInPost.getUser().getUsername();
	}
}
