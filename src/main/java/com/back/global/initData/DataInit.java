package com.back.global.initData;

import com.back.comment.entity.Comment;
import com.back.comment.repository.CommentRepository;
import com.back.member.entity.Member;
import com.back.member.repository.MemberRepository;
import com.back.post.entity.Post;
import com.back.post.repository.PostRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataInit {
    private final DataInit self;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public DataInit(
            @Lazy DataInit self,
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            PostRepository postRepository,
            CommentRepository commentRepository
    ) {
        this.self = self;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Bean
    public ApplicationRunner baseInitDataRunner() {
        return args -> {
            self.makeMembers();
            self.makePosts();
            self.makeComments();
        };
    }

    @Transactional
    public void makeMembers() {
        if(memberRepository.count() > 0) return;
        createMember("user1@test.com", "1234", "회원1");
        createMember("user2@test.com", "1234", "회원2");
    }

    @Transactional
    public void makePosts() {
        if (postRepository.count() > 0) return;

        Member user1 = memberRepository.findByEmail("user1@test.com")
                .orElseThrow();

        Member user2 = memberRepository.findByEmail("user2@test.com")
                .orElseThrow();

        for (int i = 1; i <= 15; i++) {
            Member author = i % 2 == 1 ? user1 : user2;

            Post post = new Post(
                    "게시글 " + i,
                    "게시글 " + i + "의 내용입니다.",
                    author
            );

            postRepository.save(post);
        }
    }
    @Transactional
    public void makeComments() {
        if (commentRepository.count() > 0) return;

        Member user1 = memberRepository.findByEmail("user1@test.com")
                .orElseThrow();

        Member user2 = memberRepository.findByEmail("user2@test.com")
                .orElseThrow();

        List<Post> posts = postRepository.findAll(
                Sort.by(Sort.Direction.ASC, "id")
        );

        for (int i = 0; i < 5; i++) {
            Post post = posts.get(i);

            Comment comment1 = new Comment(
                    "게시글 " + (i + 1) + "의 첫 번째 댓글입니다.",
                    user1,
                    post
            );

            Comment comment2 = new Comment(
                    "게시글 " + (i + 1) + "의 두 번째 댓글입니다.",
                    user2,
                    post
            );

            commentRepository.save(comment1);
            commentRepository.save(comment2);
        }
    }


    private void createMember(String email, String password, String nickname) {
        if (memberRepository.existsByEmail(email)) {
            return;
        }

        Member member = new Member(
                email,
                passwordEncoder.encode(password),
                nickname
        );

        memberRepository.save(member);
    }
}