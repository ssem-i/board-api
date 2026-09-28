package com.back.member.service;

import com.back.member.dto.SignupRequest;
import com.back.member.dto.SignupResponse;
import com.back.member.entity.Member;
import com.back.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponse signup(SignupRequest request) {

        if (memberRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Member member = new Member(
                request.email(),
                encodedPassword,
                request.nickname()
        );

        Member savedMember = memberRepository.save(member);

        return new SignupResponse(
                savedMember.getId(),
                savedMember.getEmail(),
                savedMember.getNickname()
        );
    }
}