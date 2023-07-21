package com.sparta.sprintbackofficeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDto {

    private Long reporterId;
    private Long reportedUserId;
    private Long postId;
    private String reason;
}