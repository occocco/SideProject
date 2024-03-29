import {getToken, getMemberId} from '/js/jwt/tokenUtil.js';

const $container = $('.container');

init();

function init() {
    $('#walletBtn').click(function () {
        getWallet();
    })
}

function getWallet() {
    fetch('/wallets/' + getMemberId(), {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + getToken(),
            'Cache-Control': 'no-cache'
        },
    })
        .then(response => response.json())
        .then(result => {
            if (result.message) {
                if (confirm(result.message + ' 지갑을 만드시겠습니까?')) {
                    $container.html(addWalletForm);
                    createWallet();
                }
            } else {
                const walletForm = WalletForm(result.name, result.balance, result.createdDate, result.updatedDate);
                $container.html(walletForm);
                goIndex();
                chargeWallet();
            }
        })
        .catch(error => console.error(error));
}

function createWallet() {
    $('#addWallet').click(function () {
        let formData = new FormData(document.getElementById('walletForm'));
        walletPostFetchApi('/wallets/', formData);
    })
}

function chargeWallet() {
    $('#chargeFormBtn').click(function () {
        $container.html(chargeWalletForm);

        $('#chargeBtn').click(function () {

            let formData = new FormData(document.getElementById('chargeWalletForm'));
            formData.append('sender', getMemberId());
            formData.append('receiver', getMemberId());

            walletPostFetchApi('/transactions', formData);
        });
    })
}

function walletPostFetchApi(uri, formData) {
    fetch(uri, {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + getToken(),
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(Object.fromEntries(formData))
    })
        .then(response => response.json())
        .then(result => {
            if (result.message) {
                alert(result.message);
            }
            getWallet();
        })
}

const addWalletForm =
    `
    <main>
        <form id="walletForm">
            <h1 class="h3 mb-3 fw-normal">지갑 만들기</h1>
            <div class="form-floating pb-1">
                <input type="text" class="form-control" name="walletName" id="walletName">
                <label for="walletName">지갑 별칭</label>
            </div>
        </form>
        <button class="w-100 btn btn-primary" id="addWallet" data-action="false">등록</button>
        <button class="w-100 btn btn-primary mt-1" id="homeBtn">홈으로</button>
    </main>
`;

const WalletForm = (name, balance, createdDate, updatedDate) => {
    return `
    <main>
        <form id="walletForm">
            <h1 class="h3 mb-3 fw-normal">나의 지갑</h1>
            <div class="form-floating pb-1">
                <input type="text" class="form-control" name="walletName" id="walletName" value="${name}" readonly>
                <label for="walletName">지갑 별칭</label>
            </div>
            <div class="form-floating pb-1">
                <input type="text" class="form-control" name="balance" id="balance" value="${balance.toLocaleString()}" readonly>
                <label for="walletName">잔액</label>
            </div>
            <div class="form-floating pb-1">
                <input type="text" class="form-control" name="createdDate" id="createdDate" value="${createdDate}" readonly>
                <label for="walletName">생성일</label>
            </div>
            <div class="form-floating pb-1">
                <input type="text" class="form-control" name="updatedDate" id="updatedDate" value="${updatedDate}" readonly>
                <label for="walletName">수정일</label>
            </div>
        </form>
        <button class="w-100 btn btn-primary mt-1" id="chargeFormBtn" data-action="false">충전하기</button>
        <button class="w-100 btn btn-primary mt-1" id="homeBtn">홈으로</button>
    </main>
`;
}

const chargeWalletForm =
    `
    <main>
        <form id="chargeWalletForm">
            <h1 class="h3 mb-3 fw-normal">지갑 충전</h1>
            <div class="form-floating pb-1">
                <input type="number" class="form-control" name="amount" id="amount" min="0">
                <label for="walletName">충전금액</label>
            </div>
        </form>
        <button class="w-100 btn btn-primary" id="chargeBtn">충전하기</button>
        <button class="w-100 btn btn-primary mt-1" id="homeBtn">홈으로</button>
    </main>
`;

function goIndex() {
    $('#homeBtn').click(function () {
        location.replace('/');
    })
}
