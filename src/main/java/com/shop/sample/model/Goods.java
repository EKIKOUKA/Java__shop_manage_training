package com.shop.sample.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sp_goods")
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long goodsId;

    private String goodsName;
    private Integer goodsPrice;
    private Integer goodsNumber;

    private Long addTime;
    private Long updTime;
}
