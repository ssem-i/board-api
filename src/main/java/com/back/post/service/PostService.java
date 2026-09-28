package com.back.post.service;

import com.back.comment.repository.CommentRepository;
import com.back.member.entity.Member;
import com.back.member.repository.MemberRepository;
import com.back.post.dto.PostListResponse;
import com.back.post.dto.PostRequest;
import com.back.post.dto.PostResponse;
import com.back.post.entity.Post;
import com.back.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponse create(PostRequest request, String email) {
        Member member = findMember(email);

        Post post = new Post(request.title(), request.content(), member);
        Post savedPost = postRepository.save(post);

        return PostResponse.from(savedPost);
    }

    @Transactional(readOnly = true)
    public Page<PostListResponse> findAll(int page, int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException("페이지 값이 올바르지 않습니다.");
        }

        return postRepository.findPostList(
                PageRequest.of(page, size)
        );
    }

    @Transactional(readOnly = true)
    public PostResponse findById(Long id) {
        Post post = findPost(id);
        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse update(Long id, PostRequest request, String email) {
        Post post = findPost(id);
        validateAuthor(post, email);
        post.update(request.title(), request.content());

        return PostResponse.from(post);
    }

    @Transactional
    public void delete(Long id, String email) {
        Post post = findPost(id);
        validateAuthor(post, email);
        commentRepository.deleteByPost_Id(id);

        postRepository.delete(post);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "게시글을 찾을 수 없습니다."
                        )
                );
    }

    private Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "회원을 찾을 수 없습니다."
                        )
                );
    }

    private void validateAuthor(Post post, String email) {
        if (!post.getAuthor().getEmail().equals(email)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "작성자만 수정하거나 삭제할 수 있습니다."
            );
        }
    }
}