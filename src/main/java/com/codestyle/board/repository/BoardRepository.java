package com.codestyle.board.repository;

import com.codestyle.board.entity.BOARD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BOARD, Integer> {
}
