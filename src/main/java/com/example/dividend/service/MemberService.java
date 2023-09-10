package com.example.dividend.service;

import com.example.dividend.model.Auth;
import com.example.dividend.model.MemberEntity;
import com.example.dividend.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;

@Service
@Slf4j
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user ->" + username));
    }

    // 회원가입 기능
    public MemberEntity register(Auth.SignUp signUp){
        // username db 존재 여부 확인
        Boolean exists = memberRepository.existsByUserName(signUp.getUsername());
        if(exists){
            throw new RuntimeException("이미 존재하는 유저입니다.");
        }
        // pw encoding
        signUp.setPassword(this.passwordEncoder.encode(signUp.getPassword()));
        // entity 로 변경하여서 repository 에 저장
        MemberEntity member = this.memberRepository.save(signUp.toEntity());
        return member;
    }

    //로그인 검증 기능
    public MemberEntity authenticate(Auth.SignIn signIn){
        return null;
    }
}
