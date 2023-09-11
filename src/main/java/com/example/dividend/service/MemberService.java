package com.example.dividend.service;

import com.example.dividend.exception.impl.AlreadyExistUserException;
import com.example.dividend.model.Auth;
import com.example.dividend.persist.entity.MemberEntity;
import com.example.dividend.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        boolean exists = this.memberRepository.existsByUsername(signUp.getUsername());
        if(exists){
            throw new AlreadyExistUserException();
        }
        // pw encoding
        signUp.setPassword(this.passwordEncoder.encode(signUp.getPassword()));
        // entity 로 변경하여서 repository 에 저장
        MemberEntity member = this.memberRepository.save(signUp.toEntity());
        return member;
    }

    //로그인 검증 기능
    public MemberEntity authenticate(Auth.SignIn signIn){
        var user = this.memberRepository.findByUsername(signIn.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID 입니다."));

        if(!this.passwordEncoder.matches(signIn.getPassword() , user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }
}
