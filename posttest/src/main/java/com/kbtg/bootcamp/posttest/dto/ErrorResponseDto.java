package com.kbtg.bootcamp.posttest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    private int status;
    private LocalDateTime timestamp;
    private String error;
    private Object errors;
    private String message;
    private String path;

    public ErrorResponseDto(int status, LocalDateTime timestamp, String error, String message, String path) {
        this.status = status;
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ErrorResponseDto(int status, LocalDateTime timestamp, Object errors, String message, String path) {
        this.status = status;
        this.timestamp = timestamp;
        this.errors = errors;
        this.message = message;
        this.path = path;
    }
}
