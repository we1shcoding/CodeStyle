package com.codestyle.board.repository;

import com.codestyle.board.entity.SignUpdata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface SignUpRepository extends JpaRepository<SignUpdata, Long> {
//    SIGNUPDATA findByEmail(String email);
//    SIGNUPDATA findByName(String name); // 사용자명으로 데이터 조회 메소드 추가
    Optional<SignUpdata> findByEmail(String email);
    Optional<SignUpdata> findByName(String name);
}
