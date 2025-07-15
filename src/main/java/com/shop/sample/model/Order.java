package com.shop.sample.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "sp_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String orderNumber;
    private String orderPrice;
    private String orderPay;
    private String orderType;
    private String orderContent;
    private String payStatus;
    private String createTime;
}
