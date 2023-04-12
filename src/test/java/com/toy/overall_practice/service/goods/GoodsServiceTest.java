package com.toy.overall_practice.service.goods;

import com.toy.overall_practice.domain.category.Category;
import com.toy.overall_practice.domain.goods.Goods;
import com.toy.overall_practice.domain.goods.repository.GoodsRepository;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.member.repository.MemberRepository;
import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.service.category.CategoryService;
import com.toy.overall_practice.service.goods.dto.GoodsCreateDto;
import com.toy.overall_practice.service.goods.dto.GoodsDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class GoodsServiceTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private GoodsRepository goodsRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private GoodsService goodsService;

    @Test
    void saveGoodsTest() {

        String categoryName = "의류";
        Category category = new Category(1L, categoryName, null, new ArrayList<>(), new ArrayList<>());
        Member member = Member.createMember("TestMember", "1234", RoleType.MEMBER);
        GoodsCreateDto goodsCreateDto = new GoodsCreateDto("상품명", categoryName, 1000L);
        Principal principal = new UsernamePasswordAuthenticationToken(member.getLoginId(), "1234");
        Goods goods = new Goods(category, goodsCreateDto.getName(), goodsCreateDto.getPrice(), member);

        when(categoryService.findCategory(categoryName)).thenReturn(category);
        when(memberRepository.findByLoginId(member.getLoginId())).thenReturn(Optional.of(member));
        when(goodsService.saveGoods(goodsCreateDto, principal)).thenReturn(goods);

        GoodsDto result = GoodsDto.toGoodsDto(goods);

        assertNotNull(result.getCategory());
        assertEquals(goodsCreateDto.getName(), result.getName());
        assertEquals(goodsCreateDto.getPrice(), result.getPrice());
        assertEquals(member.getLoginId(), result.getSeller());
    }

}