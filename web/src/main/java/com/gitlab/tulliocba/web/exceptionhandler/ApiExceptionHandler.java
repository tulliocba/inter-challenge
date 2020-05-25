package com.gitlab.tulliocba.web.exceptionhandler;

import com.gitlab.tulliocba.application.user.service.exception.RuleException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({RuleException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleRuleException(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final List<ApiError> apiErrors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> new ApiError(HttpStatus.BAD_REQUEST,
                        ((FieldError) error).getField() + " " + error.getDefaultMessage()))
                .collect(Collectors.toList());

        return handleExceptionInternal(ex, apiErrors, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);
    }

}
