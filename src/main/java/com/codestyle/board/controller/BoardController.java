package com.codestyle.board.controller;

import com.codestyle.board.entity.BOARD;
import com.codestyle.board.entity.SIGNUPDATA;
import com.codestyle.board.service.BoardService;
import com.codestyle.board.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class BoardController {

    private final BoardService boardService;
    private final SignUpService signUpService;

    @Autowired
    public BoardController(BoardService boardService, SignUpService signUpService) {
        this.boardService = boardService;
        this.signUpService = signUpService;
    }

    // 게시판 관련 엔드포인트들
    @GetMapping("/home")
    public String home() {
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

    // 회원가입 관련 엔드포인트들
    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("signUpData", new SIGNUPDATA());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpProcess(@ModelAttribute("signUpData") SIGNUPDATA signUpData, Model model) {
        try {
            // 사용자명 길이 유효성 검사 (이 예시에서는 이름을 사용자명으로 가정)
            if (signUpData.getName() != null && signUpData.getName().length() > 8) {
                throw new Exception("이름은 8자까지만 허용됩니다.");
            }

            // 사용자명 중복 체크
            if (signUpService.existsByUsername(signUpData.getName())) {
                throw new Exception("이미 사용중인 이름입니다.");
            }

            // 이메일 중복 체크
            if (signUpService.existsByEmail(signUpData.getEmail())) {
                throw new Exception("이미 존재하는 이메일입니다.");
            }

            signUpService.signUp(signUpData);
            model.addAttribute("message", "회원가입이 되었습니다!");
            return "redirect:/home"; // 회원가입 성공 후 홈 페이지로 리다이렉트
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "signup"; // 실패 시 다시 회원가입 폼으로 돌아감
        }
    }

    @PostMapping("/checkUsernameDuplicate")
    @ResponseBody
    public boolean checkUsernameDuplicate(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        return signUpService.existsByUsername(username);
    }
    @PostMapping("/checkEmailDuplicate")
    @ResponseBody
    public boolean checkEmailDuplicate(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return signUpService.existsByEmail(email);
    }
    @GetMapping("/login")
    // 로그인 관련 엔드포인트들
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginProcess() {
        // 로그인 처리 로직
        return "redirect:/home"; // 로그인 후 홈으로 리다이렉트
    }
}
