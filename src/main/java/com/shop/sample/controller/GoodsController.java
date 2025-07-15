package com.shop.sample.controller;

import com.shop.sample.model.Goods;
import com.shop.sample.repository.GoodsRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class GoodsController {
    private final GoodsRepository goodsRepository;

    public GoodsController(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @PostMapping("/getGoodsList")
    public Map<String, Object> getGoodsList(HttpServletResponse response) {
        try {
            List<Goods> goodsList = goodsRepository.findAll(Sort.by(Sort.Direction.DESC, "goodsId"));
            return Map.of("success", 1, "data", goodsList);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Map.of("success", 0, "message", "商品一覧取得に失敗しました");
        }
    }

    @PostMapping("/addGood")
    public Map<String, Object> addGood(@RequestBody Map<String, Object> request, HttpServletResponse response) {
        try {
            Goods goods = new Goods();
            goods.setGoodsName((String) request.get("goods_name"));
            goods.setGoodsNumber((Integer) request.get("goods_number"));
            goods.setGoodsPrice((Integer) request.get("goods_price"));
            long now = System.currentTimeMillis() / 1000;
            goods.setAddTime(now);
            goods.setUpdTime(now);
            goodsRepository.save(goods);

            return Map.of("success", 1, "message", "商品が追加しました");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Map.of("success", 0, "message", "商品追加に失敗しました");
        }
    }

    @PostMapping("/updateGood")
    public Map<String, Object> updateGood(@RequestBody Map<String, Object> request, HttpServletResponse response) {
        try {
            Long goodsId = Long.valueOf(request.get("goods_id").toString());
            Optional<Goods> optionalGoods = goodsRepository.findByGoodsId(goodsId);
            if (optionalGoods.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return Map.of("success", 0, "message", "対象の商品が見つかりません");
            }

            Goods goods = optionalGoods.get();
            goods.setGoodsName((String) request.get("goods_name"));
            goods.setGoodsNumber((Integer) request.get("goods_number"));
            goods.setGoodsPrice((Integer) request.get("goods_price"));
            goods.setUpdTime(System.currentTimeMillis() / 1000);

            goodsRepository.save(goods);
            return Map.of("success", 1, "message", "更新成功");
        }  catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Map.of("success", 0, "message", "商品更新に失敗しました");
        }
    }

    @PostMapping("/deleteGood")
    public Map<String, Object> deleteGood(@RequestBody Map<String, Object> request, HttpServletResponse response) {
        try {
            Long goodsId = Long.valueOf(request.get("goods_id").toString());
            if (!goodsRepository.existsById(goodsId)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return Map.of("success", 0, "message", "商品が見つかりません");
            }

            goodsRepository.deleteById(goodsId);
            return Map.of("success", 1, "message", "   ");
        }  catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Map.of("success", 0, "message", "削除失敗しました");
        }
    }
}
