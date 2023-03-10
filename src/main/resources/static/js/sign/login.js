$('#loginBtn').click(function () {
    let loginForm = new FormData(document.getElementById('loginForm'));
    login('/login', loginForm).then((res) => {
        localStorage.setItem("Authorization", "Bearer " + res.value);
        location.replace($(this).attr('aria-label'));
    })
        .catch((error) => {
            alert(error.message);
        });
})

async function login(url, data) {
    const response = await fetch(url, {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': getToken()
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(Object.fromEntries(data)),
    });
    let promise = await response.json();
    if (response.ok) {
        return promise;
    } else {
        throw new Error(promise.message)
    }
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


function getToken() {

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
                .then((response) => response.json())
                .then((data) => {
                    const refreshToken = data.value;
                    localStorage.setItem('Authorization', 'Bearer ' + refreshToken);
                    return 'Bearer ' + refreshToken;
                });
        } else {
            return token;
        }
    }
    return null;
}

