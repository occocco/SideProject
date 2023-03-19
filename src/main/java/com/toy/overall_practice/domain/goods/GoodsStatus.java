package com.toy.overall_practice.domain.goods;

public enum GoodsStatus {
    SELLING("판매중"),
    SOLD_OUT("판매완료"),
    CANCELED("판매취소");

    public final String category;

    GoodsStatus(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
