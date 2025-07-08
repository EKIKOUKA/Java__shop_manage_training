package com.shop.sample.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "sp_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Getter
    private String password;
    @Getter
    private String userEmail;

    public Long getUserId() { return user_id; }
}
