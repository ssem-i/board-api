package com.back.post.dto;

import com.back.post.entity.Post;
import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        String authorNickname,
        LocalDateTime createdTime,
        LocalDateTime updatedTime
) {

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getNickname(),
                post.getCreatedTime(),
                post.getUpdatedTime()
        );
    }
}