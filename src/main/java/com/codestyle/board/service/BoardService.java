package com.codestyle.board.service;
import com.codestyle.board.entity.Board;
import com.codestyle.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public void saveBoard(Board board) {
        board.setCreatedAt(LocalDateTime.now()); // Board 엔티티의 createdAt 필드 설정
        boardRepository.save(board); // Board 객체 저장
    }

    @Transactional(readOnly = true)
    public List<Board> getAllBoards() {
        return boardRepository.findAll(); // 모든 Board 객체 조회
    }
}

