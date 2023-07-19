package com.sparta.sprintbackofficeproject.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiException {
    private String msg;
    private Integer status;

    public ApiException(String message, Integer status) {
        this.msg = message;
        this.status = status;
    }
}