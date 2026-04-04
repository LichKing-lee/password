package com.password.api.exception;

import com.password.exception.PasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ErrorResponse> handlePasswordException(PasswordException e) {
        log.warn(e.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getUserMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected exception", e);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("서버 내부 오류가 발생했습니다."));
    }

    public record ErrorResponse(String message) {
    }
}
