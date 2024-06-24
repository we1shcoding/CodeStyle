package com.codestyle.board.service;

import com.codestyle.board.entity.BOARD;
import com.codestyle.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BoardService {

    private BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public void write(BOARD board) {
        boardRepository.save(board);
    }

    // 다른 필요한 비즈니스 로직들을 추가할 수 있습니다.
}
