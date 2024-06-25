document.addEventListener('DOMContentLoaded', function() {
    const authLinks = document.getElementById('auth-links');

    // 초기 로그인 상태 확인
    fetch('/auth-status', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            updateAuthLinks(data.loggedIn, data.username); // loggedIn과 username 전달
        })
        .catch(error => {
            console.error('Error:', error);
            // 에러 처리 로직 추가 가능
        });

    // 로그아웃 버튼 클릭 처리
    authLinks.addEventListener('click', (e) => {
        if (e.target.matches('.logout-button')) {
            fetch('/logout', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(() => {
                    // 로그아웃 성공 시 홈페이지로 이동
                    window.location.href = '/';
                })
                .catch(error => console.error('Error:', error));
        }
    });

    function updateAuthLinks(loggedIn, username) {
        if (loggedIn) {
            authLinks.innerHTML = `<form><span>${username} 님 환영합니다!</span><button type="button" class="logout-button">로그아웃</button></form>`;
        } else {
            authLinks.innerHTML = '<form><button type="button" class="buttons1" onclick="location.href=\'/login\'">로그인</button><button type="button" class="buttons1" onclick="location.href=\'/signup\'">회원가입</button></form>';
        }
    }
});
