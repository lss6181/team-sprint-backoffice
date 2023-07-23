package com.sparta.sprintbackofficeproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimelineItemDto {
    private Long id;
    private String Content;
    private String createdAt;
    private String itemType;
    private int likes;
}