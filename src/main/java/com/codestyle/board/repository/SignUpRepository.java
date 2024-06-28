package com.codestyle.board.repository;

import com.codestyle.board.entity.SignUpdata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface SignUpRepository extends JpaRepository<SignUpdata, Long> {
    Optional<SignUpdata> findByEmail(String email);
    Optional<SignUpdata> findByName(String name);
}


