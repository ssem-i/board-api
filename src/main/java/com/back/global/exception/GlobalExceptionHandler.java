package com.back.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(
            IllegalArgumentException e
    ) {
        return Map.of(
                "code", "BAD_REQUEST",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(
            MethodArgumentNotValidException e
    ) {
        return Map.of(
                "code", "BAD_REQUEST",
                "message", "입력값이 올바르지 않습니다."
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatus(
            ResponseStatusException e
    ) {
        HttpStatus status = HttpStatus.valueOf(
                e.getStatusCode().value()
        );

        return ResponseEntity.status(status).body(
                Map.of(
                        "code", status.name(),
                        "message", e.getReason() == null
                                ? "요청을 처리할 수 없습니다."
                                : e.getReason()
                )
        );
    }
}