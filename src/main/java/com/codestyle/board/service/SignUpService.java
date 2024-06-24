package com.codestyle.board.service;

import com.codestyle.board.entity.SIGNUPDATA;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.codestyle.board.repository.SignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SignUpService {
    private final SignUpRepository signUpRepository;

    @Autowired
    public SignUpService(SignUpRepository signUpRepository) {
        this.signUpRepository = signUpRepository;
    }

    @Transactional
    public boolean existsByEmail(String email) {
        return signUpRepository.findByEmail(email) != null;
    }

    @Transactional
    public void signUp(SIGNUPDATA signUpData) throws Exception {
        // 이메일 중복 체크
        if (existsByEmail(signUpData.getEmail())) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        signUpRepository.save(signUpData);
    }
}
