package com.back.post.repository;

import com.back.post.entity.Post;
import com.back.post.dto.PostListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(
            value = """
            SELECT new com.back.post.dto.PostListResponse(
                p.id,
                p.title,
                a.nickname,
                COUNT(c),
                p.createdTime,
                p.updatedTime
            )
            FROM Post p
            JOIN p.author a
            LEFT JOIN p.comments c
            GROUP BY p.id, p.title, a.nickname,
                     p.createdTime, p.updatedTime
            ORDER BY p.createdTime DESC, p.id DESC
            """,
            countQuery = "SELECT COUNT(p) FROM Post p"
    )
    Page<PostListResponse> findPostList(Pageable pageable);
}