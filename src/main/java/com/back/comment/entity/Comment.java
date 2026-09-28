package com.back.comment.entity;

import com.back.member.entity.Member;
import com.back.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @Column(nullable = false)
    private LocalDateTime updatedTime;

    public Comment(String content, Member author, Post post) {
        this.content = content;
        this.author = author;
        this.post = post;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = createdTime;
    }

    public void update(String content) {
        this.content = content;
        this.updatedTime = LocalDateTime.now();
    }

}