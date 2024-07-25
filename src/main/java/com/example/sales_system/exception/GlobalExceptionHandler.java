package com.example.sales_system.exception;

import com.example.sales_system.dto.response.AppResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<AppResponse<?>> handleException(Exception exception) {
        log.error(exception.getMessage());
        AppStatusCode statusCode = AppStatusCode.UNCATEGORIZED_ERROR;
        return ResponseEntity.status(statusCode.getHttpStatusCode())
                .body(AppResponse.builder()
                        .code(statusCode.getCode())
                        .message(statusCode.getMessage() + ": " + exception.getMessage())
                        .build());
    }

    @ExceptionHandler(AppException.class)
    ResponseEntity<AppResponse<?>> handleAppException(AppException exception) {
        AppStatusCode statusCode = exception.getAppStatusCode();
        return ResponseEntity.status(statusCode.getCode())
                .body(AppResponse.builder()
                        .code(statusCode.getCode())
                        .message(statusCode.getMessage())
                        .build());
    }


}
