document.addEventListener("DOMContentLoaded", function() {
    const password = document.getElementById("password");
    const confirmPassword = document.getElementById("confirm-password");
    const checkUsernameBtn = document.getElementById("checkUsernameBtn");
    const errorMessage = document.getElementById("error-message");
    const usernameError = document.getElementById("usernameError");
    const emailError = document.getElementById("emailError");
    const emailInput = document.getElementById("email"); // 추가

    // 비밀번호 일치 여부 확인
    confirmPassword.addEventListener("keyup", function () {
        const passwordValue = password.value.trim();
        const confirmPasswordValue = confirmPassword.value.trim();

        if (passwordValue !== confirmPasswordValue) {
            confirmPassword.setCustomValidity("비밀번호가 일치하지 않습니다.");
            errorMessage.textContent = "비밀번호가 일치하지 않습니다.";
        } else {
            confirmPassword.setCustomValidity("");
            errorMessage.textContent = "";
        }
    });

// 사용자 이름 중복 체크
    checkUsernameBtn.addEventListener("click", function() {
        const username = document.getElementById('username').value.trim();

        // AJAX를 사용하여 서버에 사용자 이름 중복 확인 요청
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/checkUsernameDuplicate", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    const response = JSON.parse(xhr.responseText);
                    if (response.duplicate) {
                        errorMessage.textContent = "이미 사용 중인 사용자 이름입니다.";
                    } else {
                        errorMessage.textContent = "사용할 수 있는 사용자 이름입니다.";
                    }
                } else {
                    errorMessage.textContent = "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
                }
            }
        };

        const data = JSON.stringify({ username: username });
        xhr.send(data);
    });

    // 이메일 중복 체크
    emailInput.addEventListener("blur", function() {
        const emailValue = emailInput.value.trim();

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/checkEmailDuplicate", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    const response = JSON.parse(xhr.responseText);
                    if (response.duplicate) {
                        emailError.textContent = "이미 사용 중인 이메일입니다.";
                    } else {
                        emailError.textContent = "";
                    }
                } else {
                    emailError.textContent = "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
                }
            }
        };

        const data = JSON.stringify({ email: emailValue });
        xhr.send(data);
    });

    // 비밀번호 보안 강도 체크 (예시)
    password.addEventListener("keyup", function() {
        const passwordValue = password.value.trim();
        let securityLevel = 0;

        // 간단한 보안 강도 체크 예시
        if (passwordValue.length >= 8) {
            securityLevel++;
        }
        if (/[a-z]/.test(passwordValue)) {
            securityLevel++;
        }
        if (/[A-Z]/.test(passwordValue)) {
            securityLevel++;
        }
        if (/\d/.test(passwordValue)) {
            securityLevel++;
        }

        // 보안 강도에 따라 메시지 출력
        const strengthMessage = document.getElementById("password-strength-message");
        if (securityLevel < 3) {
            strengthMessage.textContent = "비밀번호 보안이 낮습니다.";
        } else if (securityLevel < 4) {
            strengthMessage.textContent = "보통 수준의 비밀번호 보안입니다.";
        } else {
            strengthMessage.textContent = "높은 수준의 비밀번호 보안입니다.";
        }
    });
});
