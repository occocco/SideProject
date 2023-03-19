package com.toy.overall_practice.service.post;

import com.toy.overall_practice.domain.category.Category;
import com.toy.overall_practice.domain.goods.Goods;
import com.toy.overall_practice.domain.goods.GoodsStatus;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.post.Post;
import com.toy.overall_practice.domain.post.region.Region;
import com.toy.overall_practice.domain.post.repository.PostRepository;
import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.service.goods.GoodsService;
import com.toy.overall_practice.service.goods.dto.GoodsCreateDto;
import com.toy.overall_practice.service.post.dto.PostCreateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Principal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PostServiceTest {
    @Mock
    PostRepository postRepository;
    @Mock
    GoodsService goodsService;
    @InjectMocks
    PostService postService;

    Principal principal = new UsernamePasswordAuthenticationToken("testingMember", "123");

    @Test
    void savePostTest() {

        Member member = Member.createMember("testingMember", "123", RoleType.MEMBER);
        Category category = new Category(1L, "의류", null, new ArrayList<>(), new ArrayList<>());
        PostCreateDto postCreateDto = new PostCreateDto("TestTitle", "Test", "TestGoods", "의류", 10000L, Region.SEOUL.getCategory());
        GoodsCreateDto goodsCreateDto = new GoodsCreateDto(postCreateDto.getGoodsName(), postCreateDto.getSubCategory(), postCreateDto.getPrice(), "SELLING");
        Goods goods = new Goods(category, goodsCreateDto.getName(), goodsCreateDto.getPrice(), GoodsStatus.SELLING, member);
        Post post = new Post(postCreateDto.getTitle(), postCreateDto.getContent(), Region.SEOUL, goods);

        when(goodsService.saveGoods(goodsCreateDto, principal)).thenReturn(goods);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postService.savePost(postCreateDto, principal);

        assertNotNull(result);
        assertEquals(postCreateDto.getTitle(), result.getTitle());
        assertEquals(Region.SEOUL, result.getRegion());
        assertEquals(goods, result.getGoods());
        verify(goodsService, times(1)).saveGoods(goodsCreateDto, principal);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void savePostExTestCaseA() {
        PostCreateDto postCreateDto = new PostCreateDto("TestTitle", "Test", "TestGoods", "의류", 10000L, Region.SEOUL.getCategory());

        assertThrows(NullPointerException.class, () -> postService.savePost(postCreateDto, null));

    }

    @Test
    void savePostExTestCaseB() {

        assertThrows(NullPointerException.class, () -> postService.savePost(null, principal));
    }


}