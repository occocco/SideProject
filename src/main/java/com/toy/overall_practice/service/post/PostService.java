package com.toy.overall_practice.service.post;

import com.toy.overall_practice.domain.category.Category;
import com.toy.overall_practice.domain.goods.Goods;
import com.toy.overall_practice.domain.post.Post;
import com.toy.overall_practice.domain.post.region.Region;
import com.toy.overall_practice.domain.post.repository.PostRepository;
import com.toy.overall_practice.service.category.CategoryService;
import com.toy.overall_practice.service.goods.GoodsService;
import com.toy.overall_practice.service.goods.dto.GoodsCreateDto;
import com.toy.overall_practice.service.post.dto.PostCreateDto;
import com.toy.overall_practice.service.post.dto.PostModifyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final GoodsService goodsService;

    @Transactional
    public Post savePost(PostCreateDto postCreateDto, Principal principal) {

        Objects.requireNonNull(principal, "회원 정보를 찾을 수 없습니다.");
        Objects.requireNonNull(postCreateDto, "잘못된 요청값 입니다.");

        GoodsCreateDto goodsCreateDto = new GoodsCreateDto(postCreateDto.getGoodsName(), postCreateDto.getSubCategory(), postCreateDto.getPrice());
        Goods goods = goodsService.saveGoods(goodsCreateDto, principal);
        Post post = Post.createPost(postCreateDto.getTitle(),
                postCreateDto.getContent(),
                Region.valueOfCategory(postCreateDto.getRegion()),
                goods);
        return postRepository.save(post);
    }

    public List<Post> findPosts() {
        return postRepository.findAllSellingPosts();
    }

    public Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("게시물을 찾을 수 없습니다."));
    }

    @Transactional
    public Post patchPost(Long id, PostModifyDto modifyDto) {
        Post findPost = findPostById(id);
        Category category = categoryService.findCategory(modifyDto.getSubCategory());
        findPost.updatePost(modifyDto, category);
        return findPost;
    }

    @Transactional
    public Post modifyPostStatus(Long id, String category) {
        Post post = findPostById(id);
        post.modifyStatus(category);
        return post;
    }
}
