package com.sparta.sprintbackofficeproject.dto;

import com.sparta.sprintbackofficeproject.exception.ApiException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyResponseDto extends ApiException {

    public ModifyResponseDto(String message, int status) {
    super.setMsg(message);
    super.setStatus(status);
    }
}
