import {getToken, getMemberId} from '/js/jwt/tokenUtil.js';
import {goIndex} from '/js/index.js';

const $container = $('body');
const temp =
    `
    <main>
        <div style="margin: 0 10px; display: flex; justify-content: center;">
        <h1 class="h3 mb-3">거래내역</h1>
        </div>
        <div>
            <div class="cards d-flex row">
        
            </div>
        </div>
        <button class="btn btn-primary mt-1" id="homeBtn" style="position: fixed; top: 0; left: 0;">홈으로</button>
    </main>
    `
init();

function init() {
    $('#txRecordBtn').click(function () {
        getTxRecord();
    })
}

function getTxRecord() {
    fetch('/transactions/' + getMemberId(), {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + getToken(),
            'Cache-Control': 'no-cache'
        },
    })
        .then(response => response.json())
        .then(result => {
            if (result.message) {
                alert(result.message);
            } else {
                let txLists = '';
                for (const tx of result) {
                    const txList = txListForm(tx.sender, tx.receiver, tx.amount, tx.balance, tx.createdDate);
                    txLists += txList;
                }
                $container.html(temp);
                $('.cards').html(txLists)
                goIndex();
            }
        })
        .catch(error => console.error(error));
}

const txListForm = (sender, receiver, amount, balance, createdDate) => {
    return `
<div style="margin-left: 22rem">
    <div class="flex-row w-50">
        <div class="card justify-content-center col">
            <div class="card-body">
                <div class="row col">
                    <div class="col-6">
                        <h6>보낸사람: ${sender}</h6>
                        <h6>금액: ${amount.toLocaleString()}</h6>
                        <h6>거래일: ${createdDate}</h6>
                    </div>
                    <div class="col-6">
                        <h6>받는사람: ${receiver}</h6>
                        <h6>지갑잔액: ${balance.toLocaleString()}</h6>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
`;
}
