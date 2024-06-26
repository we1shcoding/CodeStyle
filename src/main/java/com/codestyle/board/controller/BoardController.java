package com.codestyle.board.controller;

import com.codestyle.board.entity.Board;
import com.codestyle.board.entity.SignUpdata;
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
import java.util.List;

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

    @GetMapping("/write")
    public String showWriteForm(Model model) {
        model.addAttribute("board", new Board()); // 폼 데이터를 바인딩할 빈 Board 객체 생성
        return "board1"; // board1.html을 응답으로 보냄
    }

    // 게시글 작성 처리
    @PostMapping("/write")
    public String handleWriteForm(@ModelAttribute("board") Board board, HttpSession session) {
        try {
            // 세션에서 사용자 이름을 가져옴
            String username = (String) session.getAttribute("username");

            if (username == null) {
                // 로그인이 되어 있지 않은 경우 로그인 페이지로 리다이렉트
                return "redirect:/login";
            }

            // 사용자명을 Board 객체에 설정
            board.setWriter(username);

            // Board 객체의 content 필드 검증
            if (board.getContent() == null || board.getContent().isEmpty()) {
                throw new IllegalArgumentException("내용을 입력해주세요.");
            }

            // Board 작성 서비스 메소드 호출
            boardService.saveBoard(board); // 데이터베이스에 게시글 저장

            // 게시글 작성 후 게시글 목록 페이지로 리다이렉트
            return "redirect:/board1"; // 수정: board1 페이지로 리다이렉트
        } catch (Exception e) {
            // 예외 발생 시 처리
            e.printStackTrace(); // 또는 로그로 기록
            return "error"; // 예외 발생 시 에러 페이지로 리다이렉트 또는 에러 메시지 반환
        }
    }

    // 게시글 목록 페이지
    @GetMapping("/board1")
    public String showBoardList(Model model) {
        List<Board> boardList = boardService.getAllBoards(); // 모든 게시글 가져오기
        model.addAttribute("boardList", boardList);
        return "board1"; // board1.html에 게시글 목록을 보여주기 위해 boardList를 전달
    }

    // 회원가입 관련 엔드포인트들
    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("signUpData", new SignUpdata());
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
            SignUpdata data = new SignUpdata();
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