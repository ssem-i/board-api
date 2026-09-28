package com.back.comment.repository;

import com.back.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
            SELECT c FROM Comment c
            JOIN FETCH c.author
            WHERE c.post.id = :postId
            ORDER BY c.createdTime ASC, c.id ASC
            """)
    List<Comment> findAllByPostId(@Param("postId") Long postId);

    void deleteByPost_Id(Long postId);
}