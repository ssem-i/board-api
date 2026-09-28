package com.back.comment.dto;

import com.back.comment.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long postId,
        String content,
        String authorNickname,
        LocalDateTime createdTime,
        LocalDateTime updatedTime
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getContent(),
                comment.getAuthor().getNickname(),
                comment.getCreatedTime(),
                comment.getUpdatedTime()
        );
    }
}