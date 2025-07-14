package com.shop.sample.controller;

import com.shop.sample.model.Order;
import com.shop.sample.repository.OrderRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping("/getOrdersList")
    public Map<String, Object> getOrdersList(HttpServletResponse response) {

        try {
            List<Order> ordersResult = orderRepository.findAll();

            return Map.of(
                "success", 1,
                "data", ordersResult
            );
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            return Map.of(
                "success", 0,
                "message", "注文一覧取得に失敗しました"
            );
        }
    }
}
