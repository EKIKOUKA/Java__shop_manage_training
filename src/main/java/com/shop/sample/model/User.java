package com.shop.sample.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sp_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long userId;

    private String userEmail;
    private String username;
    private String avatar;
    private String gender;
    private String totpSecret;
    private byte userEdu;
    private byte isActive;

    @Setter(AccessLevel.NONE)
    private String password;
}
