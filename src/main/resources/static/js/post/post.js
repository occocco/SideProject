import {getToken} from '/js/jwt/tokenUtil.js';

const $container = $('.container');

$('#addPostBtn').click(function () {
    $container.html(addPostForm);
    selectedOpt();
    getRegions();
    insertPost();
})

function selectedOpt() {
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

function getChildCategories(parentsCategory) {
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

function getRegions() {
    fetch('/regions/', {
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
                }
                let post = postForm(result.title, result.content, result.goodsName, result.category, result.price, result.region, result.createdDate, result.updatedDate);
                $container.html(post);
            })
    })
}

const addPostForm =
    `
<main>
    <h1>판매글 등록</h1>
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
</main>
    `;

const postForm = (title, content, goodsName, category, price, region, createdDate, updatedDate) => {

    return `
<main>
    <h1>판매글 조회</h1>
    <form id="postForm">
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
      <label for="goodsName">작성일</label><br>
      <input class="form-control" type="text" id="createdDate" name="createdDate" value="${createdDate}" readonly><br>
      <label for="goodsName">수정일</label><br>
      <input class="form-control" type="text" id="updatedDate" name="updatedDate" value="${updatedDate}" readonly><br>
    </form>
      <button class="btn btn-primary mt-1">목록으로</button>
</main>
    `;

}


