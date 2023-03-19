package com.toy.overall_practice.domain.goods.repository;

import com.toy.overall_practice.domain.goods.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
}
