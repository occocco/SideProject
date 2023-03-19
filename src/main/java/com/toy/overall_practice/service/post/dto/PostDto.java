package com.toy.overall_practice.service.post.dto;

import com.toy.overall_practice.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private String title;

    private String content;

    private String goodsName;

    private String category;

    private Long price;

    private String region;

    private String createdDate;

    private String updatedDate;

    public static PostDto toPostDto(Post post) {
        return new PostDto(
                post.getTitle(),
                post.getContent(),
                post.getGoods().getName(),
                post.getGoods().getCategory().getName(),
                post.getGoods().getPrice(),
                post.getRegion().getCategory(),
                post.getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")),
                post.getUpdatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")));
    }
}
