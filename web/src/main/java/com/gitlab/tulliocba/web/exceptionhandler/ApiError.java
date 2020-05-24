package com.gitlab.tulliocba.web.exceptionhandler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@ApiModel("Error")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ApiError {

    @ApiModelProperty(notes = "HTTP Status Code")
    private HttpStatus status;
    @ApiModelProperty(notes = "Detailed message")
    private String message;
    @ApiModelProperty(notes = "Error time")
    private LocalDateTime time;

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.time = LocalDateTime.now();
    }
}
