package com.toy.overall_practice.domain.goods;

import com.toy.overall_practice.domain.category.Category;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    private Long id;

    private String name;

    private Long price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private GoodsStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "seller_id", referencedColumnName = "member_id")
    private Member seller;

    @OneToOne(fetch = LAZY, mappedBy = "goods")
    private Post post;

    public Goods(Category category, String name, Long price, GoodsStatus status, Member seller) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.status = status;
        this.seller = seller;
    }

    public void connectPost(Post post) {
        this.post = post;
    }
}
