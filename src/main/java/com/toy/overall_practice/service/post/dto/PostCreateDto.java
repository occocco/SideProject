package com.toy.overall_practice.service.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {

    private String title;

    private String content;

    private String goodsName;

    private String subCategory;

    private Long price;

    private String region;

}
