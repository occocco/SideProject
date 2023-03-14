$('#loginBtn').click(function () {
    let loginForm = new FormData(document.getElementById('loginForm'));
    login('/login', loginForm).then((res) => {
        if (res.message) {
            alert(res.message);
        } else {
        localStorage.setItem("Authorization", "Bearer " + res.value);
        location.replace($(this).attr('aria-label'));
        }
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
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(Object.fromEntries(data)),
    });
    return response.json();
}

