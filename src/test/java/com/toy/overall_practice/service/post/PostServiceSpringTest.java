package com.toy.overall_practice.service.post;

import com.toy.overall_practice.domain.post.Post;
import com.toy.overall_practice.domain.post.region.Region;
import com.toy.overall_practice.service.post.dto.PostCreateDto;
import com.toy.overall_practice.service.post.dto.PostModifyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PostServiceSpringTest {

    @Autowired
    PostService postService;
    @Autowired
    EntityManager entityManager;
    PostCreateDto postCreateDto;
    Principal principal;
    PostModifyDto postModifyDto;

    @BeforeEach
    public void init() {
        postCreateDto = new PostCreateDto("TestTitle",
                "Test",
                "TestGoods",
                "의류",
                10000L,
                Region.SEOUL.getCategory());
        principal = new UsernamePasswordAuthenticationToken("MemberC", "123");

        postModifyDto =
                new PostModifyDto("modifyTitle",
                        "modifyContent",
                        "modifyGoodsName",
                        "남성의류",
                        2000L,
                        Region.GYEONGGI.getCategory());
    }

    @Test
    void savePostTest() {

        Post post = postService.savePost(postCreateDto, principal);

        assertNotNull(post);
        assertEquals(postCreateDto.getTitle(), post.getTitle());
        assertEquals(postCreateDto.getGoodsName(), post.getGoods().getName());
    }

    @Test
    void savePostExTestPrincipalIsNull() {

        assertThrows(NullPointerException.class, () -> postService.savePost(postCreateDto, null));

    }

    @Test
    void savePostExTestisPostDtoIsNull() {

        assertThrows(NullPointerException.class, () -> postService.savePost(null, principal));

    }

    @Test
    void findPostsTest() {

        postService.savePost(postCreateDto, principal);

        List<Post> result = postService.findPosts();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findPostByIdTest() {

        Post post = postService.savePost(postCreateDto, principal);
        Long id = post.getId();

        Post result = postService.findPostById(id);

        assertNotNull(result);
        assertEquals(post, result);
        assertEquals(post.getId(), result.getId());
        assertEquals(post.getGoods(), result.getGoods());
    }

    @Test
    void findPostByIdExTest_WrongId() {
        Long wrongId = 999L;
        postService.savePost(postCreateDto, principal);

        assertThatThrownBy(() -> postService.findPostById(wrongId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("게시물을 찾을 수 없습니다.");
    }

    @Test
    void patchPostTest() {

        Post post = postService.savePost(postCreateDto, principal);

        Post updatedPost = postService.patchPost(post.getId(), postModifyDto);

        assertNotNull(updatedPost);
        assertEquals(post.getTitle(), "modifyTitle");
        assertEquals(post.getTitle(), updatedPost.getTitle());
        assertEquals(post.getGoods().getPrice(), 2000L);
    }

    @Test
    void patchPostExTest_NotFoundRegion() {

        Post post = postService.savePost(postCreateDto, principal);
        String notSupportedRegion = "TOKYO";

        postModifyDto.setRegion(notSupportedRegion);

        assertThrows(IllegalArgumentException.class, () -> postService.patchPost(post.getId(), postModifyDto));
        assertThatThrownBy(() -> postService.patchPost(post.getId(), postModifyDto))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("지원하지 않는 지역입니다.");

    }

    @Test
    void patchPostUpdatedDateTest() throws InterruptedException {
        int second = 3;

        Post post = postService.savePost(postCreateDto, principal);
        entityManager.flush();
        LocalDateTime updatedDate = post.getUpdatedDate();

        Thread.sleep(second * 1000L);
        Post updatedPost = postService.patchPost(post.getId(), postModifyDto);
        entityManager.flush();
        LocalDateTime patchPostUpdatedDate = updatedPost.getUpdatedDate();

        Duration duration = Duration.between(updatedDate, patchPostUpdatedDate);

        assertNotEquals(updatedDate, patchPostUpdatedDate);
        assertTrue(duration.getSeconds() >= second);
    }
}
