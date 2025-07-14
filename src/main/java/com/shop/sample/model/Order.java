package com.shop.sample.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "sp_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    @JsonProperty("order_id")
    private Long orderId;

    private String order_number;
    private String order_price;
    private String order_pay;
    private String order_type;
    private String order_content;
    private String pay_status;
    private String create_time;
}
