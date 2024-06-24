package com.codestyle.board.controller;
import com.codestyle.board.entity.BOARD;
import com.codestyle.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {

@Autowired
    private BoardService boardService;

    @GetMapping("/home")
    public String boardWriteFrom() {
        return "home";
    }

    @PostMapping("/board/writepro")
    public String boardWrite(BOARD board) {
        boardService.write(board);
        return "redirect:/home";
    }

    @GetMapping("/board1")
    public String board1() {
        return "board1";
    }
}
