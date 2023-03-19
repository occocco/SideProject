package com.toy.overall_practice.service.goods.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCreateDto {

    private String name;
    private String category;
    private Long price;
    private String status;

}
