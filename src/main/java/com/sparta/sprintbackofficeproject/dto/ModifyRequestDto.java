package com.sparta.sprintbackofficeproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyRequestDto {
    String email;
    String imageUrl;
    String introduction;
    String password;
}
