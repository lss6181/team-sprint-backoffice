package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.TagUserInPost;
import lombok.Getter;

@Getter
public class TagUserResponseDto {
	private Long tagId;
	private String tagUserName;

	public TagUserResponseDto(TagUserInPost tagUserInPost) {
		this.tagId = tagUserInPost.getId();
		tagUserName = tagUserInPost.getUser().getUsername();
	}
}
