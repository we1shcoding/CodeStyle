// 이메일 중복
function checkUsername() {
    const username = document.getElementById('username').value;
    alert(`사용자 이름 "${username}"의 중복을 체크합니다.`);
}

// 비밀번호 일치 여부
const password = document.getElementById("password");
const confirmPassword = document.getElementById("confirm-password");

confirmPassword.addEventListener("keyup", function () {
    if (password.value !== confirmPassword.value) {
        confirmPassword.setCustomValidity("비밀번호가 일치하지 않습니다.");
    } else {
        confirmPassword.setCustomValidity("");
    }
});
