package com.toy.overall_practice.domain.post;

public enum PostStatus {
    SELLING("판매중"),
    SOLD_OUT("판매완료"),
    CANCELED("판매취소");

    public final String category;

    PostStatus(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public static PostStatus valueOfCategory(String category) {
        for (PostStatus postStatus : values()) {
            if (postStatus.category.equals(category)) {
                return postStatus;
            }
        }
        throw new IllegalArgumentException("잘못된 판매 상태입니다.");
    }
}
