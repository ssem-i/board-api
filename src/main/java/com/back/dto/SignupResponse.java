package com.back.dto;

public record SignupResponse(
        Long id,
        String email,
        String nickname
) {
}