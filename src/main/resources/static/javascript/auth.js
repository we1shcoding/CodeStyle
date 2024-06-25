document.addEventListener('DOMContentLoaded', function() {
    const authLinks = document.getElementById('auth-links');

    if (!authLinks) {
        console.error('Element with id "auth-links" not found.');
        return;
    }

    // 함수 정의: 팝업 메시지 표시
    function showPopupMessage(username) {
        const message = `${username} 님 로그인 성공!`;
        alert(message);
    }

    // 로그인 상태 확인 (서버에서 가져오는 것으로 가정)
    fetch('/auth-status', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log(data); // 데이터 로깅
            if (data.loggedIn && data.username) {
                const username = data.username;
                showPopupMessage(username); // 팝업 메시지 표시
                authLinks.innerHTML = `<button type="button" class="logout-button">로그아웃</button>`;
            } else {
                authLinks.innerHTML = '<form><button type="button" class="buttons1" onclick="location.href=\'/login\'">로그인</button><button type="button" class="buttons1" onclick="location.href=\'/signup\'">회원가입</button></form>';
            }
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
});
