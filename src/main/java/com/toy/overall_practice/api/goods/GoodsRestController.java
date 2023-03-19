package com.toy.overall_practice.api.goods;

import com.toy.overall_practice.domain.goods.Goods;
import com.toy.overall_practice.service.goods.GoodsService;
import com.toy.overall_practice.service.goods.dto.GoodsCreateDto;
import com.toy.overall_practice.service.goods.dto.GoodsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class GoodsRestController {

    private final GoodsService goodsService;

    @PostMapping("/goods")
    public ResponseEntity<GoodsDto> saveGoods(@RequestBody GoodsCreateDto goodsCreateDto, Principal principal) {
        Goods goods = goodsService.saveGoods(goodsCreateDto, principal);
        GoodsDto goodsDto = GoodsDto.toGoodsDto(goods);
        return ResponseEntity.ok().body(goodsDto);
    }
}
