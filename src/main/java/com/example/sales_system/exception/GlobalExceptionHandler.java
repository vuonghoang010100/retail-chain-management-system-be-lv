package com.example.sales_system.exception;

import com.example.sales_system.dto.response.AppResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse<?>> handleException(Exception exception) {
        log.error(exception.getClass().getName(), exception);
        log.error(exception.getMessage());
        AppStatusCode statusCode = AppStatusCode.UNCATEGORIZED_ERROR;
        return generateResponseEntity(statusCode, exception.getMessage());
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<AppResponse<?>> handleAppException(AppException exception) {
        AppStatusCode statusCode = exception.getAppStatusCode();
        return generateResponseEntity(statusCode);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<AppResponse<?>> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException exception) {
        AppStatusCode statusCode = AppStatusCode.INVALID_ARGUMENT;
        return generateResponseEntity(statusCode, exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<AppResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        AppStatusCode statusCode = AppStatusCode.UNCATEGORIZED_ERROR;
        if (StringUtils.containsIgnoreCase(
                exception.getMessage(),
                "JSON parse error: Cannot deserialize value of type `java.time.LocalDate` from String")) {
            statusCode = AppStatusCode.INVALID_DATE;
        }
        if (StringUtils.containsIgnoreCase(
                exception.getMessage(),
                "JSON parse error: Cannot deserialize value of type `com.example.sales_system.enums.Gender` from String"
        )) {
            statusCode = AppStatusCode.INVALID_GENDER;
        }

        if (statusCode == AppStatusCode.UNCATEGORIZED_ERROR) {
            log.error(exception.getClass().getName(), exception);
            log.error(exception.getMessage(), exception);
            log.warn("Need process error at handleHttpMessageNotReadableException");
        }

        return generateResponseEntity(statusCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        AppStatusCode statusCode = AppStatusCode.INVALID_KEY;

        String status_key = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        try {
            statusCode = AppStatusCode.valueOf(status_key);
        } catch (IllegalArgumentException e) {
            log.error(exception.getMessage(), exception);
        }

        if (statusCode == AppStatusCode.INVALID_KEY) {
            log.error(exception.getClass().getName(), exception);
            log.error(exception.getMessage(), exception);
            log.warn("Need process error at handleMethodArgumentNotValidException");
        }

        return generateResponseEntity(statusCode);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AppResponse<?>> handleIllegalArgumentException(IllegalArgumentException exception) {
        AppStatusCode statusCode = AppStatusCode.ILLEGAL_ARGUMENT;

        if (StringUtils.containsIgnoreCase(exception.getMessage(),
                "Page index must not be less than zero")) {
            return generateResponseEntity(AppStatusCode.INVALID_PAGE);
        }
        else if (StringUtils.containsIgnoreCase(exception.getMessage(),
                "Page size must not be less than zero")) {
            return generateResponseEntity(AppStatusCode.INVALID_PAGE_SIZE);
        }

        return generateResponseEntity(statusCode, exception.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<AppResponse<?>> handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
        AppStatusCode statusCode = AppStatusCode.UNAUTHORIZED;
        return generateResponseEntity(statusCode);
    }

    private ResponseEntity<AppResponse<?>> generateResponseEntity(AppStatusCode statusCode, String message) {
        String msg = StringUtils.isEmpty(message) ? statusCode.getMessage() : statusCode.getMessage() + ": " + message;
        return ResponseEntity.status(statusCode.getHttpStatusCode())
                .body(AppResponse.builder()
                        .code(statusCode.getCode())
                        .message(msg)
                        .build());
    }

    private ResponseEntity<AppResponse<?>> generateResponseEntity(AppStatusCode statusCode) {
        return generateResponseEntity(statusCode, null);
    }

}
