import {getToken, getMemberId} from '/js/jwt/tokenUtil.js';

const $container = $('.container');

$('#member-info').click(function () {
    getInfo();
})

function modify() {
    $('#modifyBtn').click(function () {
        let flag = $(this).attr('data-action');
        if (flag === 'false') {

            let passwordTag = $(this).prev().children('div').eq(1).children('input');
            passwordTag.removeAttr('disabled');

            $(this).attr('data-action', 'true');
        } else if (flag === 'true') {

            let formData = new FormData(document.getElementById('InfoForm'));

            modifyInfo('/members/' + getMemberId(), formData)
                .then((response) => {
                    if (response.message) {
                        alert(response.message);
                    } else {
                        getInfo();
                    }
                })

            $(this).attr('data-action', 'false');
        }
    })
}

function getInfo() {
    fetch('/members/' + getMemberId(), {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + getToken(),
            'Cache-Control': 'no-cache'
        },
    })
        .then(response => response.json())
        .then(memberDto => {
            let infoConst = memberInfo(memberDto.loginId, memberDto.password, memberDto.roleType);
            $container.html(infoConst);

            modify();
            goIndex();

        })
        .catch(error => console.error(error));
}


async function modifyInfo(url, data) {
    const response = await fetch(url, {
        method: 'PATCH',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getToken()
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(Object.fromEntries(data)),
    });
    return response.json();

}


function goIndex() {
    $('#homeBtn').click(function () {
        location.replace('/');
    })
}

const memberInfo = (id, password, type) => {
    return `
    <main>
        <form id="InfoForm">
            <h1 class="h3 mb-3 fw-normal">회원정보</h1>
            <div class="form-floating pb-1">
                <input type="text" class="form-control" name="loginId" id="loginId" autocomplete="username"
                       value="${id}" readonly>
                <label for="password">Id</label>
            </div>
            <div class="form-floating pb-1">
                <input type="password" class="form-control" name="password" id="password" autocomplete="current-password"
                       value="${password}" disabled>
                <label for="password">password</label>
            </div>
            <div class="form-floating pb-1">
                <input type="text" class="form-control" name="role" id="type" autocomplete="current-password"
                       value="${type}" readonly>
                <label for="type">MemberType</label>
            </div>
            
        </form>
        <button class="w-100 btn btn-primary" id="modifyBtn" data-action="false">회원정보수정</button>
        <button class="w-100 btn btn-primary mt-1" id="homeBtn">홈으로</button>
    </main>
`;
}
