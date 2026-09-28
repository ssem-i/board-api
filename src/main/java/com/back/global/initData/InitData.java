package com.back.global.initData;

import com.back.member.entity.Member;
import com.back.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createMember("user1@test.com", "1234", "회원1");
        createMember("user2@test.com", "1234", "회원2");
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