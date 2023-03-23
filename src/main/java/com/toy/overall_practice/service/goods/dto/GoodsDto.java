package com.toy.overall_practice.service.goods.dto;

import com.toy.overall_practice.domain.goods.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDto {

    private String name;

    private Long price;

    private String category;

    private String seller;

    public static GoodsDto toGoodsDto(Goods goods) {
        return new GoodsDto(
                goods.getName(),
                goods.getPrice(),
                goods.getCategory().getName(),
                goods.getSeller().getLoginId()
        );
    }
}
