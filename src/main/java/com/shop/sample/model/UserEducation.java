package com.shop.sample.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "sp_education")
public class UserEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eduId;

    private String eduValue;
    private Integer sortOrder;
}
