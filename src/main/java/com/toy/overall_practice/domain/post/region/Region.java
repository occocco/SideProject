package com.toy.overall_practice.domain.post.region;

public enum Region {

    SEOUL("서울"),
    GYEONGGI("경기"),
    ETC("기타");

    private final String category;

    Region(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public static Region valueOfCategory(String category) {
        for (Region region : values()) {
            if (region.category.equals(category)) {
                return region;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 지역입니다.");
    }
}

