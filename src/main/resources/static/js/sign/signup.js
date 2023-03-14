
$('#signupBtn').click(function () {
    let signupForm = new FormData(document.getElementById('signupForm'));
    signup('/members', signupForm)
        .then((response)=>{
            if (response.message) {
                alert(response.message);
            } else {
                location.replace('/login?redirect=/')
            }
        })
})

async function signup(url, data) {
    const response = await fetch(url, {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json'
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(Object.fromEntries(data)),
    });
        return response.json();
}