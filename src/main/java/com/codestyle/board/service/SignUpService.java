package com.codestyle.board.service;

import com.codestyle.board.entity.SignUpdata;
import com.codestyle.board.repository.SignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SignUpService {

    private final SignUpRepository signUpRepository;

    @Autowired
    public SignUpService(SignUpRepository signUpRepository) {
        this.signUpRepository = signUpRepository;
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return signUpRepository.findByEmail(email).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return signUpRepository.findByName(username).isPresent();
    }

    @Transactional
    public void signUp(SignUpdata signUpData) throws Exception {
        // 사용자명 중복 체크
        if (existsByUsername(signUpData.getName())) {
            throw new Exception("이미 사용중인 이름입니다.");
        }

        // 이메일 중복 체크
        if (existsByEmail(signUpData.getEmail())) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        signUpRepository.save(signUpData);
    }

    public boolean authenticate(String email, String password) throws Exception {
        Optional<SignUpdata> userOpt = signUpRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            SignUpdata user = userOpt.get();
            // 비밀번호 검증 (그냥 단순 문자열만 비교)
            return user.getPassword().equals(password);
        } else {
            return false;
        }
    }

    public String getUsernameByEmail(String email) {
        Optional<SignUpdata> userOpt = signUpRepository.findByEmail(email);
        return userOpt.map(SignUpdata::getName).orElse(null);
    }
}
