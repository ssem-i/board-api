package com.back.comment.controller;

import com.back.comment.dto.CommentRequest;
import com.back.comment.dto.CommentResponse;
import com.back.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> create(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication
    ) {
        CommentResponse response = commentService.create(
                postId,
                authentication.getName(),
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> findByPostId(
            @PathVariable Long postId
    ) {
        return commentService.findByPostId(postId);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentResponse update(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication
    ) {
        return commentService.update(
                commentId,
                authentication.getName(),
                request
        );
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        commentService.delete(commentId, authentication.getName());

        return ResponseEntity.noContent().build();
    }
}