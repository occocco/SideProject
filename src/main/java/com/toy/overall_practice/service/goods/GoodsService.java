package com.toy.overall_practice.service.goods;

import com.toy.overall_practice.domain.category.Category;
import com.toy.overall_practice.domain.goods.Goods;
import com.toy.overall_practice.domain.goods.GoodsStatus;
import com.toy.overall_practice.domain.goods.repository.GoodsRepository;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.member.repository.MemberRepository;
import com.toy.overall_practice.service.category.CategoryService;
import com.toy.overall_practice.service.goods.dto.GoodsCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoodsService {

    private final CategoryService categoryService;
    private final GoodsRepository goodsRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public Goods saveGoods(GoodsCreateDto goodsCreateDto, Principal principal) {

        Category category = categoryService.findCategory(goodsCreateDto.getCategory());

        GoodsStatus goodsStatus = validationStatus(goodsCreateDto.getStatus());

        Member member = memberRepository.findByLoginId(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));

        Goods goods = new Goods(
                category,
                goodsCreateDto.getName(),
                goodsCreateDto.getPrice(),
                goodsStatus,
                member);

        return goodsRepository.save(goods);

    }

    private GoodsStatus validationStatus(String status) {
        try {
            return GoodsStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 상품 상태입니다.");
        }
    }

}
