package com.sparta.sprintbackofficeproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeDto {
    private Long id;
    private String title;
    private String content;

    public NoticeDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
