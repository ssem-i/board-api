package com.back.post.dto;

import java.time.LocalDateTime;

public record PostListResponse(
        Long id,
        String title,
        String authorNickname,
        Long commentCount,
        LocalDateTime createdTime,
        LocalDateTime updatedTime
) {
}