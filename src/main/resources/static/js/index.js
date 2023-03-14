import {getToken} from '/js/jwt/tokenUtil.js';

const $container = $('.container');
const token = localStorage.getItem('Authorization');
const redirectUri = $container.data('redirect');

if (token) {
    const signForm = `
    <a type="button" class="btn btn-primary" role="button" href="/login?redirect=${redirectUri}">로그인상태</a>
    <a type="button" class="btn btn-primary" role="button" id="member-info">회원정보</a>
    <a type="button" class="btn btn-primary" role="button" id="logoutBtn">로그아웃</a>
    `;
    $container.html(signForm)
} else {
    const signForm = `
    <a type="button" class="btn btn-primary" role="button" href="/login?redirect=${redirectUri}">로그인</a>
    <a type="button" class="btn btn-primary" role="button" href="/join">회원가입</a>
    `;
    $container.html(signForm)
}

$('#logoutBtn').click(function () {
    fetch('/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getToken()
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
    }).then((response) => {
        if (response.status === 200) {
            localStorage.removeItem('Authorization');
            location.replace('/')
        }
    });

})