export function getToken() {

    const token = localStorage.getItem('Authorization');

    if (token) {
        const decodedToken = decodeToken(token);
        if (decodedToken.exp < Date.now() / 1000) {
            return fetch('/token/reissue', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                },
            })
                .then((response) => {
                    if (response.status === 403) {
                        alert('인증 정보가 만료되었습니다. 다시 로그인 해주세요.');
                        location.replace('/login?redirect=/')
                    }
                    return response.json();
                })
                .then((data) => {
                    const refreshToken = data.value;
                    localStorage.setItem('Authorization', 'Bearer ' + refreshToken);
                    return 'Bearer ' + refreshToken;
                });
        } else {
            return token.substring(7);
        }
    }
    localStorage.removeItem('Authorization')
    return null;
}

const decodeToken = (token) => {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
        atob(base64)
            .split('')
            .map(function (c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            })
            .join('')
    );
    return JSON.parse(jsonPayload);
}

export function getMemberId() {

    const token = localStorage.getItem('Authorization');
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload).sub;

}