package com.toy.overall_practice.service.post;

import com.toy.overall_practice.domain.post.Post;
import com.toy.overall_practice.domain.post.region.Region;
import com.toy.overall_practice.service.post.dto.PostCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PostServiceSpringTest {

    @Autowired
    PostService postService;

    @Test
    void savePostTest() {

        Principal principal = new UsernamePasswordAuthenticationToken("MemberC", "123");
        PostCreateDto postCreateDto = new PostCreateDto("TestTitle", "Test", "TestGoods", "의류", 10000L, Region.SEOUL.getCategory());

        Post post = postService.savePost(postCreateDto, principal);

        assertNotNull(post);
        assertEquals(postCreateDto.getTitle(), post.getTitle());
        assertEquals(postCreateDto.getGoodsName(), post.getGoods().getName());
    }

    @Test
    void savePostExTestPrincipalIsNull(){

        PostCreateDto postCreateDto = new PostCreateDto("TestTitle", "Test", "TestGoods", "의류", 10000L, Region.SEOUL.getCategory());

        assertThrows(NullPointerException.class, ()->postService.savePost(postCreateDto, null));

    }

    @Test
    void savePostExTestisPostDtoIsNull(){

        Principal principal = new UsernamePasswordAuthenticationToken("MemberC", "123");

        assertThrows(NullPointerException.class, ()->postService.savePost(null, principal));

    }
}
