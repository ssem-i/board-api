package com.back.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "댓글 내용을 입력해주세요.")
        @Size(max = 255, message = "댓글은 255자 이하로 입력해주세요.")
        String content
) {
}