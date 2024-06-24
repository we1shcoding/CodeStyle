package com.codestyle.board.service;

import com.codestyle.board.entity.SIGNUPDATA;
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
    public void signUp(SIGNUPDATA signUpData) throws Exception {
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
}