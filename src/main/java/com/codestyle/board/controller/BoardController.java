package com.codestyle.board.controller;

import com.codestyle.board.entity.BOARD;
import com.codestyle.board.entity.SIGNUPDATA;
import com.codestyle.board.service.BoardService;
import com.codestyle.board.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
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
    @ResponseBody // JSON 응답을 위해 필요
    @Transactional
    public ResponseEntity<Map<String, Object>> signUpProcess(@RequestBody Map<String, String> signUpData) {
        Map<String, Object> response = new HashMap<>();

        try {
            String username = signUpData.get("username");
            String email = signUpData.get("email");
            String password = signUpData.get("password");

            // 사용자명 유효성 검사
            if (username == null || username.isEmpty()) {
                throw new Exception("이름을 입력해주세요.");
            }
            // 사용자명 길이 유효성 검사 (이 예시에서는 이름을 사용자명으로 가정)
            if (username.length() > 8) {
                throw new Exception("이름은 8자까지만 허용됩니다.");
            }
            // 사용자명 중복 체크
            if (signUpService.existsByUsername(username)) {
                throw new Exception("이미 사용중인 이름입니다.");
            }
            // 이메일 중복 체크
            if (signUpService.existsByEmail(email)) {
                throw new Exception("이미 존재하는 이메일입니다.");
            }

            // 서비스 레이어에서 회원가입 처리
            SIGNUPDATA data = new SIGNUPDATA();
            data.setName(username);
            data.setEmail(email);
            data.setPassword(password);

            signUpService.signUp(data);

            response.put("success", true);
            response.put("message", "회원가입이 성공적으로 완료되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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

    // 로그인 관련 엔드포인트들
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> loginProcess(@RequestBody Map<String, String> loginData, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = loginData.get("email");
            String password = loginData.get("password");

            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                throw new Exception("이메일과 비밀번호를 입력해주세요.");
            }

            // 서비스 레이어에서 로그인 처리
            boolean isAuthenticated = signUpService.authenticate(email, password);

            if (isAuthenticated) {
                // 사용자 이름 가져오기
                String username = signUpService.getUsernameByEmail(email);

                session.setAttribute("loggedIn", true); // 세션에 로그인 상태 저장
                session.setAttribute("username", username); // 세션에 사용자 이름 저장

                response.put("success", true);
                response.put("message", "로그인 성공");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "이메일 또는 비밀번호가 잘못되었습니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/auth-status")
    @ResponseBody
    public Map<String, Object> getAuthStatus(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Object loggedIn = session.getAttribute("loggedIn");
        Object username = session.getAttribute("username");
        response.put("loggedIn", loggedIn != null && (boolean) loggedIn);
        response.put("username", username != null ? username.toString() : null);
        return response;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedIn"); // 로그인 상태 제거
        session.removeAttribute("username"); // 사용자 이름 제거
        return "redirect:/home";
    }
}
