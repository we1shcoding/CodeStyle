package com.codestyle.board.service;

import com.codestyle.board.entity.SIGNUPDATA;
import com.codestyle.board.repository.SignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean existsByUsername(String username) {
        return signUpRepository.findByName(username) != null;
    }

    @Transactional
    public void signUp(SIGNUPDATA signUpData) throws Exception {
        // 이메일 중복 체크
        if (existsByEmail(signUpData.getEmail())) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        // 사용자명 중복 체크
        if (existsByUsername(signUpData.getName())) {
            throw new Exception("이미 사용중인 이름입니다.");
        }

        signUpRepository.save(signUpData);
    }
}
