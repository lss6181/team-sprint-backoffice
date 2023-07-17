package com.sparta.sprintbackofficeproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailRequestDto {
    @NotBlank
    @Email
    private String email;
}
