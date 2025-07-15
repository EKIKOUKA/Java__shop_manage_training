package com.shop.sample.repository;

import com.shop.sample.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    Optional<Goods> findByGoodsId(Long goodsId);
}
