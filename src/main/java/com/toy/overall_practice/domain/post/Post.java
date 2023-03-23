package com.toy.overall_practice.domain.post;

import com.toy.overall_practice.domain.category.Category;
import com.toy.overall_practice.domain.goods.Goods;
import com.toy.overall_practice.domain.post.region.Region;
import com.toy.overall_practice.service.post.dto.PostModifyDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private Region region;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updatedDate;

    private Post(String title, String content, Region region, Goods goods) {
        this.title = title;
        this.content = content;
        this.region = region;
        this.goods = goods;
        this.status = PostStatus.SELLING;
        connectGoods();
    }

    public static Post createPost(String title, String content, Region region, Goods goods) {
        return new Post(title, content, region, goods);
    }

    public void updatePost(PostModifyDto modifyDto, Category category) {
        this.title = modifyDto.getTitle();
        this.content = modifyDto.getContent();
        this.region = Region.valueOfCategory(modifyDto.getRegion());
        goods.updateGoods(modifyDto.getGoodsName(), category, modifyDto.getPrice());
    }

    public void connectGoods() {
        goods.connectPost(this);
    }
    public void modifyStatus(String category) {
        this.status = PostStatus.valueOfCategory(category);
    }
}
