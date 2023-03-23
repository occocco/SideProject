package com.toy.overall_practice.service.goods;

import com.toy.overall_practice.domain.goods.Goods;
import com.toy.overall_practice.domain.post.PostStatus;
import com.toy.overall_practice.service.goods.dto.GoodsCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoodsServiceSpringTest {

    @Autowired
    GoodsService goodsService;

    @Test
    void saveGoodsTest() {
        GoodsCreateDto goodsCreateDto = new GoodsCreateDto("테스트 상품", "의류", 10000L);
        Principal principal = new UsernamePasswordAuthenticationToken("TestingMember", "123");

        Goods goods = goodsService.saveGoods(goodsCreateDto, principal);

        assertNotNull(goods);
        assertEquals(goodsCreateDto.getName(), goods.getName());
        assertEquals(goods.getSeller().getLoginId(), principal.getName());
    }

}