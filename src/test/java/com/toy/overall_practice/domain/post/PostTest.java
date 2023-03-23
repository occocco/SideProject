package com.toy.overall_practice.domain.post;

import com.toy.overall_practice.domain.category.Category;
import com.toy.overall_practice.domain.goods.Goods;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.post.region.Region;
import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.service.post.dto.PostModifyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    Category category;
    Member member;
    Goods goods;

    String categoryName;

    @BeforeEach
    public void init() {
        categoryName = "전자제품";
        category = new Category(1L, categoryName, null, new ArrayList<>(), new ArrayList<>());
        member = Member.createMember("testMember", "1234", RoleType.MEMBER);
        goods = new Goods(1L, "testGoods", 1000L, category, member, null);
    }

    @Test
    void createPostTest() {
        Post post = Post.createPost("testTitle", "content", Region.SEOUL, goods);

        assertNotNull(post);
        assertEquals(category.getName(), post.getGoods().getCategory().getName());
        assertEquals(member.getLoginId(), post.getGoods().getSeller().getLoginId());
        assertEquals(goods.getPost(), post);
        assertEquals(goods.getCategory(), post.getGoods().getCategory());
        assertEquals(post.getGoods().getCategory().getName(), categoryName);

    }

    @Test
    void connectGoodsTest() {

        Goods goods = new Goods(1L, "connectGoods", 2000L, category, member, null);

        assertNull(goods.getPost());

        Post post = Post.createPost("testPost", "postTest", Region.SEOUL, goods);

        assertNotNull(goods.getPost());
        assertEquals(goods.getPost(), post);
    }

    @Test
    void updatePostTest() {
        Post post = Post.createPost("testPost", "postTest", Region.SEOUL, goods);

        String modifyTitle = "modifyTitle";
        String modifyContent = "modifyContent";
        String modifyGoodsName = "modifyGoodsName";
        Region modifyRegion = Region.GYEONGGI;

        PostModifyDto postModifyDto =
                new PostModifyDto(modifyTitle,
                        modifyContent,
                        modifyGoodsName,
                        "남성의류",
                        2000L,
                        modifyRegion.getCategory());

        post.updatePost(postModifyDto, category);

        assertEquals(post.getTitle(), modifyTitle);
        assertEquals(post.getContent(), modifyContent);
        assertEquals(post.getGoods().getName(), modifyGoodsName);
        assertEquals(post.getRegion(), modifyRegion);
    }


    @Test
    void updatePostExTest_RegionIsNull() {
        Post post = Post.createPost("testPost", "postTest", Region.SEOUL, goods);

        String modifyTitle = "modifyTitle";
        String modifyContent = "modifyContent";
        String modifyGoodsName = "modifyGoodsName";
        String modifyRegion = null;

        PostModifyDto postModifyDto =
                new PostModifyDto(modifyTitle,
                        modifyContent,
                        modifyGoodsName,
                        "남성의류",
                        2000L,
                        modifyRegion);

        assertThrows(IllegalArgumentException.class, () -> post.updatePost(postModifyDto, category));

    }

    @Test
    void modifyStatusTest() {

        Post post = Post.createPost("testPost", "postTest", Region.SEOUL, goods);
        PostStatus beforeStatus = post.getStatus();
        String modifyStatus = PostStatus.CANCELED.category;

        post.modifyStatus(modifyStatus);
        PostStatus afterStatus = post.getStatus();

        assertEquals(beforeStatus, PostStatus.SELLING);
        assertEquals(afterStatus, PostStatus.valueOfCategory(modifyStatus));

    }
}