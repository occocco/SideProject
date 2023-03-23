import {addModifyBtn, addPostForm, getRegions, postDetails, selectedOpt} from '/js/post/addPost.js'
import {getMemberId, getToken} from "/js/jwt/tokenUtil.js";
import {backPostList} from "/js/post/postList.js";

const $container = $('.container');

export function updatePost() {
    $('#updatePost').click(function () {
        let postId = getModifyForm();
        selectedOpt();
        getRegions();
        patchPost(postId);
    })
}

function patchPost(postId) {
    $('#addPost').click(function () {
        let formData = new FormData(document.getElementById('addPostForm'));
        let data = JSON.stringify(Object.fromEntries(formData));
        patchFetch('/posts/' + postId, data);
    })
}

function getModifyForm() {
    let postId = $('form').data('id');
    let title = $('#title').val();
    let goodsName = $('#goodsName').val();
    let content = $('#content').val();
    let price = $('#price').val();

    let modifyForm = addPostForm('판매글 수정');
    $container.html(modifyForm);
    $('#title').val(title);
    $('#goodsName').val(goodsName);
    $('#content').val(content);
    $('#price').val(price);

    return postId;
}

function patchFetch(uri, data) {

    fetch(uri, {
        method: 'PATCH',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getToken()
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: data
    })
        .then(response => response.json())
        .then(post => {
            if (post.message) {
                alert(post.message);
            } else {
                let postDetail = postDetails(post);
                $container.html(postDetail);
                backPostList();
                if (post.memberId === getMemberId()) {
                    addModifyBtn();
                    updatePost();
                    modifyPostStatus();
                    deletePost();
                }
            }
        })
        .catch(error => console.error(error));

}

export function modifyPostStatus() {
    $('#modifyStatusBtn').click(function () {
        let postId = $('form').data('id');
        $('#status').remove();
        const selectStatus =
            `<select class="form-select" id="status" name="status" style="border-color: orangered">
                <option value="판매중">판매중</option>
                <option value="판매완료">판매완료</option>
             <option value="판매취소">판매취소</option>
             </select>`;
        $('label[for=status]').append(selectStatus);
        $('#status').on('change', function () {
            let status = {'status': $(this).val()};
            patchFetch('/posts/' + postId + '/status', JSON.stringify(status));
        });
    })
}

export function deletePost() {
    $('#deletePost').click(function () {
        console.log('deletePost')
    })
}
