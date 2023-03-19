package com.toy.overall_practice.service.post;

import com.toy.overall_practice.domain.goods.Goods;
import com.toy.overall_practice.domain.post.Post;
import com.toy.overall_practice.domain.post.region.Region;
import com.toy.overall_practice.domain.post.repository.PostRepository;
import com.toy.overall_practice.service.goods.GoodsService;
import com.toy.overall_practice.service.goods.dto.GoodsCreateDto;
import com.toy.overall_practice.service.post.dto.PostCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final GoodsService goodsService;

    @Transactional
    public Post savePost(PostCreateDto postCreateDto, Principal principal) {

        Objects.requireNonNull(principal, "회원 정보를 찾을 수 없습니다.");
        Objects.requireNonNull(postCreateDto, "잘못된 요청값 입니다.");

        GoodsCreateDto goodsCreateDto = new GoodsCreateDto(postCreateDto.getGoodsName(), postCreateDto.getSubCategory(), postCreateDto.getPrice(), "SELLING");
        Goods goods = goodsService.saveGoods(goodsCreateDto, principal);
        Post post = Post.createPost(postCreateDto.getTitle(),
                postCreateDto.getContent(),
                Region.valueOfCategory(postCreateDto.getRegion()),
                goods);
        goods.connectPost(post);
        return postRepository.save(post);
    }
}
