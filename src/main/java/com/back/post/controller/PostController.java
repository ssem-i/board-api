package com.back.post.controller;

import com.back.post.dto.PostListResponse;
import com.back.post.dto.PostRequest;
import com.back.post.dto.PostResponse;
import com.back.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@Valid @RequestBody PostRequest request,
            Authentication authentication
    ) {
        return postService.create(request, authentication.getName());
    }

    @GetMapping
    public Page<PostListResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.findAll(page, size);
    }

    @GetMapping("/{id}")
    public PostResponse findById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @PutMapping("/{id}")
    public PostResponse update(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest request,
            Authentication authentication
    ) {
        return postService.update(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
            Authentication authentication
    ) {
        postService.delete(id, authentication.getName());

        return ResponseEntity.noContent().build();
    }
}