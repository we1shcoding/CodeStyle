document.addEventListener('DOMContentLoaded', (event) => {
    document.querySelector('.login-form').addEventListener('submit', function (e) {
        e.preventDefault(); // 기본 폼 제출 동작을 막음

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        // 서버에 로그인 요청
        fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('로그인 성공!');
                    window.location.href = '/home'; // 로그인 성공 후 홈으로 이동
                } else {
                    alert('로그인 실패: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('로그인 중 오류가 발생했습니다.');
            });
    });
});
