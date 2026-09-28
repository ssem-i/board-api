package com.back.member.dto;

public record SignupResponse(
        Long id,
        String email,
        String nickname
) {
}