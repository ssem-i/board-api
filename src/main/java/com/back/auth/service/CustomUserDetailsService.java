package com.back.auth.service;


import com.back.member.entity.Member;
import com.back.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("회원을 찾을 수 없습니다.")
                );

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles("USER")
                .build();
    }
}