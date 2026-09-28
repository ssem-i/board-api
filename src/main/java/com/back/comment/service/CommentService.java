package com.back.comment.service;

import com.back.comment.dto.CommentRequest;
import com.back.comment.dto.CommentResponse;
import com.back.comment.entity.Comment;
import com.back.comment.repository.CommentRepository;
import com.back.member.entity.Member;
import com.back.member.repository.MemberRepository;
import com.back.post.entity.Post;
import com.back.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommentResponse create(Long postId, String email, CommentRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."
                ));

        Member author = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."
                ));

        Comment comment = new Comment(request.content(), author, post);
        commentRepository.save(comment);

        return CommentResponse.from(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findByPostId(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."
            );
        }

        return commentRepository.findAllByPostId(postId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public CommentResponse update(
            Long commentId,
            String email,
            CommentRequest request
    ) {
        Comment comment = findComment(commentId);
        checkAuthor(comment, email);
        comment.update(request.content());

        return CommentResponse.from(comment);
    }

    @Transactional
    public void delete(Long commentId, String email) {
        Comment comment = findComment(commentId);
        checkAuthor(comment, email);

        commentRepository.delete(comment);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."
                ));
    }

    private void checkAuthor(Comment comment, String email) {

        if (!comment.getAuthor().getEmail().equals(email)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "댓글 작성자만 수정하거나 삭제할 수 있습니다."
            );
        }
    }
}