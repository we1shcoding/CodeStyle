document.addEventListener("DOMContentLoaded", function() {
    const usernameInput = document.getElementById("username");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const confirmPasswordInput = document.getElementById("confirm-password");
    const errorMessage = document.getElementById("error-message");
    const emailError = document.getElementById("emailError");
    const confirmPasswordError = document.getElementById("confirmPasswordError");
    const signupError = document.getElementById("signupError");
    const signupMessage = document.getElementById("signupMessage");

    // 비밀번호 일치 여부 확인
    confirmPasswordInput.addEventListener("keyup", function () {
        const passwordValue = passwordInput.value.trim();
        const confirmPasswordValue = confirmPasswordInput.value.trim();

        if (passwordValue !== confirmPasswordValue) {
            confirmPasswordInput.setCustomValidity("비밀번호가 일치하지 않습니다.");
            confirmPasswordError.textContent = "비밀번호가 일치하지 않습니다.";
        } else {
            confirmPasswordInput.setCustomValidity("");
            confirmPasswordError.textContent = "";
        }
    });

// username 중복 확인 요청
    document.getElementById("checkUsernameBtn").addEventListener("click", function () {
        const username = usernameInput.value.trim();
        if (username.length === 0) {
            errorMessage.textContent = "사용자 이름을 입력하세요.";
            return;
        }
        errorMessage.textContent = ""; // 초기화

        const xhrUsername = new XMLHttpRequest();
        xhrUsername.open("POST", "/checkUsernameDuplicate", true);
        xhrUsername.setRequestHeader("Content-Type", "application/json");
        xhrUsername.onreadystatechange = function () {
            if (xhrUsername.readyState === XMLHttpRequest.DONE) {
                if (xhrUsername.status === 200) {
                    const response = JSON.parse(xhrUsername.responseText);
                    if (response) {
                        errorMessage.textContent = "이미 사용 중인 사용자 이름입니다.";
                    } else {
                        errorMessage.textContent = "사용할 수 있는 사용자 이름입니다.";
                    }
                } else {
                    errorMessage.textContent = "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
                }
            }
        };
        const dataUsername = JSON.stringify({username: username});
        xhrUsername.send(dataUsername);
    });

// email 중복 확인 요청
    document.getElementById("checkEmailBtn").addEventListener("click", function () {
        const email = emailInput.value.trim();
        if (email.length === 0) {
            emailError.textContent = "이메일을 입력하세요.";
            return;
        }
        emailError.textContent = ""; // 초기화

        const xhrEmail = new XMLHttpRequest();
        xhrEmail.open("POST", "/checkEmailDuplicate", true);
        xhrEmail.setRequestHeader("Content-Type", "application/json");
        xhrEmail.onreadystatechange = function () {
            if (xhrEmail.readyState === XMLHttpRequest.DONE) {
                if (xhrEmail.status === 200) {
                    const response = JSON.parse(xhrEmail.responseText);
                    if (response) {
                        emailError.textContent = "이미 사용 중인 이메일입니다.";
                    } else {
                        emailError.textContent = "";
                    }
                } else {
                    emailError.textContent = "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
                }
            }
        };
        const dataEmail = JSON.stringify({email: email});
        xhrEmail.send(dataEmail);
    });

// 회원가입 폼 submit 시
    document.querySelector(".signup-form").addEventListener("submit", function (event) {
        event.preventDefault();

        const username = usernameInput.value.trim();
        const email = emailInput.value.trim();
        const password = passwordInput.value.trim();
        const confirmPassword = confirmPasswordInput.value.trim();

        // 추가적인 클라이언트 측 검증 로직 (예: 비밀번호 길이, 특수문자 포함 여부 등)

        // 서버로 전송
        const xhrSignup = new XMLHttpRequest();
        xhrSignup.open("POST", "/signup", true);
        xhrSignup.setRequestHeader("Content-Type", "application/json");
        xhrSignup.onreadystatechange = function () {
            if (xhrSignup.readyState === XMLHttpRequest.DONE) {
                if (xhrSignup.status === 200) {
                    signupMessage.textContent = "회원가입 성공!";
                    signupError.textContent = ""; // 에러 메시지 초기화
                } else {
                    signupMessage.textContent = ""; // 성공 메시지 초기화
                    signupError.textContent = "회원가입 실패. 서버 오류가 발생했습니다."; // 서버 오류 메시지 출력
                }
            }
        };
        const dataSignup = JSON.stringify({username: username, email: email, password: password});
        xhrSignup.send(dataSignup);
    });
});