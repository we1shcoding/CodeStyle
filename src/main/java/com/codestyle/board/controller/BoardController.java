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
    @GetMapping("/signup")
    public String signUpForm() {
        // 회원가입 폼을 보여주는 로직 추가
        return "signup"; // signup.html과 연결
    }

    @PostMapping("/signup")
    public String signUpProcess() {
        // 회원가입 데이터를 처리하는 로직 추가
        return "redirect:/home"; // 회원가입 후 홈으로 리다이렉트
    }

    @GetMapping("/login")
    public String loginForm() {
        // 회원가입 폼을 보여주는 로직 추가
        return "login"; // signup.html과 연결
    }

    @PostMapping("/login")
    public String loginProcess() {
        // 회원가입 데이터를 처리하는 로직 추가
        return "redirect:/home"; // 회원가입 후 홈으로 리다이렉트
    }
}
