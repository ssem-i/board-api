package com.back.global.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException e
    ) {
        return ResponseEntity.badRequest().body(
                errorBody(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatus(
            ResponseStatusException e
    ) {
        HttpStatusCode status = e.getStatusCode();

        String message = e.getReason() == null
                ? "요청을 처리할 수 없습니다."
                : e.getReason();

        return ResponseEntity.status(status).body(
                errorBody(status, message)
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthentication(
            AuthenticationException e
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                errorBody(
                        HttpStatus.UNAUTHORIZED,
                        "이메일 또는 비밀번호가 올바르지 않습니다."
                )
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String message = switch (status.value()) {
            case 400 -> "입력값이 올바르지 않습니다.";
            case 404 -> "요청한 리소스를 찾을 수 없습니다.";
            case 405 -> "지원하지 않는 HTTP 메서드입니다.";
            default -> "요청을 처리할 수 없습니다.";
        };

        return super.handleExceptionInternal(
                ex,
                errorBody(status, message),
                headers,
                status,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(
            Exception e
    ) {
        logger.error("예상하지 못한 오류가 발생했습니다.", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                errorBody(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "서버 내부 오류가 발생했습니다."
                )
        );
    }

    private Map<String, String> errorBody(
            HttpStatusCode status,
            String message
    ) {
        HttpStatus httpStatus = HttpStatus.resolve(status.value());

        String code = httpStatus == null
                ? "HTTP_" + status.value()
                : httpStatus.name();

        return Map.of(
                "code", code,
                "message", message == null
                        ? "요청을 처리할 수 없습니다."
                        : message
        );
    }
}