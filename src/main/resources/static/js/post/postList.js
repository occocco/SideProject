import {getMemberId, getToken} from '/js/jwt/tokenUtil.js';
import {postDetails, addModifyBtn} from '/js/post/addPost.js';
import {updatePost, deletePost, modifyPostStatus} from '/js/post/modifyPost.js';
import {goIndex} from '/js/index.js';

const $container = $('.container');

$('#postListBtn').click(function () {
    $container.html(postDefaultForm);
    getPostList();
    goIndex();
})

function getPostList() {
    fetch('/posts', {
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
                let posts = '';
                $.each(result, function (index, post) {
                    let listForm = postListForm(
                        post.id,
                        post.category,
                        post.region,
                        post.title,
                        post.content,
                        post.goodsName,
                        post.price,
                        post.createdDate,
                        post.updatedDate);
                    posts += listForm;
                })
                updateView(posts);
            }
        })
        .catch(error => console.error(error));
}

export function backPostList() {
    $('#backPostList').click(function () {
        $container.html(postDefaultForm);
        getPostList();
    })
}

function updateView(posts) {
    $('.postBody').html(posts);


    $('.posts').click(function () {
        let postId = $(this).data('id');
        fetch('/posts/' + postId, {
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
            .catch(error => console.error(error));
    })

}


const postListForm = (id, category, region, title, content, goodsName, price, createdDate, updatedDate) => {
    return `
                <tr class="posts" data-id="${id}">
                    <td>${category}</td>
                    <td>${region}</td>
                    <td>${title}</td>
                    <td>${content}</td>
                    <td>${goodsName}</td>
                    <td>${price}</td>
                    <td>${createdDate}</td>
                    <td>${updatedDate}</td>
                </tr>
`;
};

const postDefaultForm =
    `
        <main>
            <table>
                <thead>
                <tr>
                    <th>Category</th>
                    <th>Region</th>
                    <th>Title</th>
                    <th>Content</th>
                    <th>Goods Name</th>
                    <th>Price</th>
                    <th>Created Date</th>
                    <th>Updated Date</th>
                </tr>
                </thead>
                <tbody class="postBody">
                </tbody>
            </table>
            <button class="btn btn-primary mt-1" id="homeBtn" style="position: fixed; top: 0; left: 0;">홈으로</button>
        </main>
`;