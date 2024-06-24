package com.codestyle.board.repository;

import com.codestyle.board.entity.SIGNUPDATA;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SignUpRepository extends JpaRepository<SIGNUPDATA, Long> {
    @Query(value = "SELECT s from  SIGNUPDATA s WHERE s.email = ?1")
    SIGNUPDATA findByEmail(String email);
}
