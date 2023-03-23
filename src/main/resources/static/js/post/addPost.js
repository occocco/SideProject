import {getMemberId, getToken} from '/js/jwt/tokenUtil.js';
import {backPostList} from '/js/post/postList.js';
import {updatePost, deletePost, modifyPostStatus} from '/js/post/modifyPost.js';
import {goIndex} from '/js/index.js';

const $container = $('.container');

$('#addPostBtn').click(function () {
    $container.html(addPostForm('판매글 등록'));
    selectedOpt();
    getRegions();
    insertPost();
    goIndex();
})

export function selectedOpt() {
    $('#mainCategory').on('change', function () {
        let selectedOption = $(this).val();
        if (selectedOption !== '대분류') {
            getChildCategories(selectedOption);
        } else {
            const $subCategory = $('#subCategory');
            $subCategory.empty();
            $subCategory.append('<option value="소분류">소분류</option>');
        }
    })
}

export function getChildCategories(parentsCategory) {
    fetch('/categories/' + encodeURI(parentsCategory), {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getToken()
        },
    })
        .then(response => response.json())
        .then((response) => {
            if (response.message) {
                alert(response.message);
            } else {
                const $subCategory = $('#subCategory');
                $subCategory.empty();
                $.each(response, function (index, category) {
                    $subCategory.append('<option value="' + category.name + '">' + category.name + '</option>');
                });
            }
        })
        .catch(error => console.error(error));
}

export function getRegions() {
    fetch('/regions', {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getToken()
        },
    })
        .then(response => response.json())
        .then((response) => {
            if (response.message) {
                alert(response.message);
            } else {
                const $region = $('#region');
                $region.empty();
                $.each(response, function (index, region) {
                    $region.append('<option value="' + region + '">' + region + '</option>');
                });
            }
        })
        .catch(error => console.error(error));
}

export function postDetails(result) {
    return postForm(
        result.id,
        result.memberId,
        result.title,
        result.content,
        result.goodsName,
        result.category,
        result.price,
        result.region,
        result.status,
        result.createdDate,
        result.updatedDate);
}
function insertPost() {

    $('#addPost').click(function () {
        let formData = new FormData(document.getElementById('addPostForm'));
        fetch('/posts', {
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
                } else {
                    let post = postDetails(result);
                    $container.html(post);
                    backPostList();
                    if (result.memberId === getMemberId()) {
                        addModifyBtn();
                        updatePost();
                        modifyPostStatus();
                        deletePost();
                    }
                }
            })
    })
}

export function addModifyBtn() {
    let $button = $('#backPostList');
    const modifyButtons =
        `<button class="btn btn-primary mt-1" id="updatePost">글수정하기</button>
           <button class="btn btn-primary mt-1" id="modifyStatusBtn">판매상태변경</button>
           <button class="btn btn-primary mt-1" id="deletePost">삭제하기</button>
        `;
    $button.after(modifyButtons);
}

export const addPostForm = (title) =>
    `
<main>
    <h1>${title}</h1>
    <form id="addPostForm">
      <label for="title">제목</label><br>
      <input class="form-control" type="text" id="title" name="title"><br>
      <label for="goodsName">제품명</label><br>
      <input class="form-control" type="text" id="goodsName" name="goodsName"><br>
      <label for="content">내용</label><br>
      <textarea class="form-control" id="content" name="content"></textarea><br>
      <label for="category">카테고리</label><br>
      <select class="form-select" id="mainCategory">
      <option value="대분류">대분류</option>
      <option value="의류">의류</option>
      <option value="전자제품">전자제품</option>
      </select>
      <select class="form-select" id="subCategory" name="subCategory">
      <option value="">소분류</option>
      </select>
      <br>
      <label for="price">가격</label><br>
      <input class="form-control" type="number" id="price" name="price" min="0"><br>
      <label for="region">지역</label><br>
      <select class="form-select" id="region" name="region">
      </select>
      <br>
    </form>
      <button class="btn btn-primary mt-1" id="addPost">등록</button>
      <button class="btn btn-primary mt-1" id="homeBtn" style="position: fixed; top: 0; left: 0;">홈으로</button>
</main>
    `;

const postForm = (id, memberId, title, content, goodsName, category, price, region, status,createdDate, updatedDate) => {

    return `
<main>
    <h1>판매글 조회</h1>
    <form id="postForm" data-id="${id}">
      <label for="memberId">판매자</label><br>
      <input class="form-control" type="text" id="memberId" name="memberId" value="${memberId}" readonly><br>
      <label for="title">제목</label><br>
      <input class="form-control" type="text" id="title" name="title" value="${title}" readonly><br>
      <label for="goodsName">제품명</label><br>
      <input class="form-control" type="text" id="goodsName" name="goodsName" value="${goodsName}" readonly><br>
      <label for="content">내용</label><br>
      <input class="form-control" id="content" name="content" value="${content}" readonly><br>
      <label for="category">카테고리</label><br>
      <input class="form-control" id="subCategory" name="subCategory" value="${category}" readonly>
      <br>
      <label for="price">가격</label><br>
      <input class="form-control" type="number" id="price" name="price" min="0" value="${price}" readonly><br>
      <label for="region">지역</label><br>
      <input class="form-control" id="region" name="region" value="${region}" readonly>
      <label for="status">판매상태</label><br>
      <input class="form-control" type="text" id="status" name="status" value="${status}" readonly><br>
      <label for="goodsName">작성일</label><br>
      <input class="form-control" type="text" id="createdDate" name="createdDate" value="${createdDate}" readonly><br>
      <label for="goodsName">수정일</label><br>
      <input class="form-control" type="text" id="updatedDate" name="updatedDate" value="${updatedDate}" readonly><br>
    </form>
      <button class="btn btn-primary mt-1" id="backPostList" style="position: fixed; top: 0; left: 0;">목록으로</button>
</main>
    `;

}


