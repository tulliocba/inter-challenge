package com.gitlab.tulliocba.web.exceptionhandler;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private HttpStatus status;
    private String message;
    private LocalDateTime time;

    private ApiError() {
    }

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.time = LocalDateTime.now();
    }
}
