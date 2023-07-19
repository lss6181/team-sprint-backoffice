package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.entity.HashTag;

public class HashTagResponseDto {
	private Long hashTagId;
	private String hashTag;

	public HashTagResponseDto(HashTag hashTag) {
		this.hashTagId = hashTag.getId();
		this.hashTag = hashTag.getHashTag();
	}
}
