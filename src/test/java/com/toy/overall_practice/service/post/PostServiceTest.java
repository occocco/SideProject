package com.toy.overall_practice.service.post;

import com.toy.overall_practice.domain.category.Category;
import com.toy.overall_practice.domain.goods.Goods;
import com.toy.overall_practice.domain.post.PostStatus;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.post.Post;
import com.toy.overall_practice.domain.post.region.Region;
import com.toy.overall_practice.domain.post.repository.PostRepository;
import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.service.category.CategoryService;
import com.toy.overall_practice.service.goods.GoodsService;
import com.toy.overall_practice.service.goods.dto.GoodsCreateDto;
import com.toy.overall_practice.service.post.dto.PostCreateDto;
import com.toy.overall_practice.service.post.dto.PostModifyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PostServiceTest {
    @Mock
    PostRepository postRepository;
    @Mock
    GoodsService goodsService;
    @Mock
    CategoryService categoryService;
    @InjectMocks
    PostService postService;

    Principal principal;
    Member member;
    Category category;
    PostCreateDto postCreateDto;
    GoodsCreateDto goodsCreateDto;
    Goods goods;
    Post post;

    @BeforeEach
    public void init() {
        principal = new UsernamePasswordAuthenticationToken("testingMember", "123");
        member = Member.createMember("testingMember", "123", RoleType.MEMBER);
        category = new Category(1L, "의류", null, new ArrayList<>(), new ArrayList<>());
        postCreateDto = new PostCreateDto("TestTitle", "Test", "TestGoods", "의류", 10000L, Region.SEOUL.getCategory());
        goodsCreateDto = new GoodsCreateDto(postCreateDto.getGoodsName(), postCreateDto.getSubCategory(), postCreateDto.getPrice());
        goods = new Goods(category, goodsCreateDto.getName(), goodsCreateDto.getPrice(), member);
        post = Post.createPost(postCreateDto.getTitle(), postCreateDto.getContent(), Region.SEOUL, goods);
    }

    @Test
    void savePostTest() {

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

    @Test
    void findPostByIdTest() {
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Post result = postService.findPostById(postId);

        assertNotNull(result);
        assertEquals(post.getTitle(), result.getTitle());
        assertEquals(post.getGoods(), result.getGoods());
        verify(postRepository, times(1)).findById(postId);

    }

    @Test
    void findPostByIdExTest_WrongId() {
        Long postId = 1L;
        Long WrongId = 2L;

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertThrows(NoSuchElementException.class,
                () -> postService.findPostById(WrongId),
                "게시물을 찾을 수 없습니다.");
    }

    @Test
    void findPostsTest() {
        List<Post> postList = List.of(post);

        when(postRepository.findAllSellingPosts()).thenReturn(postList);
        List<Post> result = postService.findPosts();

        assertEquals(1, result.size());
        assertEquals(postList, result);
    }

    @Test
    void patchPostTest() {
        PostModifyDto postModifyDto =
                new PostModifyDto("modifyTitle",
                        "modifyContent",
                        "modifyGoodsName",
                        "남성의류",
                        2000L,
                        Region.SEOUL.getCategory());

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Post updatedPost = postService.patchPost(1L, postModifyDto);

        assertEquals(postModifyDto.getTitle(), updatedPost.getTitle());
        assertEquals(postModifyDto.getContent(), updatedPost.getContent());
        assertEquals(Region.SEOUL, updatedPost.getRegion());
        assertEquals(postModifyDto.getGoodsName(), updatedPost.getGoods().getName());
        assertEquals(postModifyDto.getPrice(), updatedPost.getGoods().getPrice());
    }

    @Test
    void patchPostExTest_NotFoundCategory() {

        String wrongCategory = "Wrong";
        PostModifyDto postModifyDto =
                new PostModifyDto("modifyTitle",
                        "modifyContent",
                        "modifyGoodsName",
                        wrongCategory,
                        2000L,
                        Region.SEOUL.getCategory());

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(categoryService.findCategory(wrongCategory)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> postService.patchPost(1L, postModifyDto));

    }

    @Test
    void modifyPostStatusTest() {
        Long id = 1L;
        PostStatus beforeStatus = post.getStatus();
        PostStatus modifyStatus = PostStatus.CANCELED;
        when(postRepository.findById(id)).thenReturn(Optional.of(post));

        Post modifyPost = postService.modifyPostStatus(id, modifyStatus.category);

        PostStatus afterStatus = modifyPost.getStatus();

        assertEquals(modifyStatus, afterStatus);
        assertNotEquals(beforeStatus, afterStatus);
    }

    @Test
    void modifyPostStatusExTest_StatusIsInvalid() {

        Long id = 1L;
        String invalidStatus = "잘못된 상태";
        when(postRepository.findById(id)).thenReturn(Optional.of(post));

        assertThrows(IllegalArgumentException.class, () -> postService.modifyPostStatus(id, invalidStatus));

    }

    @Test
    void modifyPostStatusExTest_NotFoundPost() {

        Long id = 1L;
        Long invalidId = 999L;
        when(postRepository.findById(id)).thenReturn(Optional.of(post));

        assertThrows(NoSuchElementException.class, () -> postService.modifyPostStatus(invalidId, PostStatus.SOLD_OUT.category));

    }
}