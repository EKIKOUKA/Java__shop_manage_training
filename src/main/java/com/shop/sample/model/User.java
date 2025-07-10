package com.shop.sample.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "sp_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Setter
    @Column(name = "user_email")
    private String userEmail;
    @Setter
    private String username;
    @Setter
    private String avatar;
    @Setter
    private String gender;
    @Setter
    @Column(name = "totp_secret")
    private String totpSecret;
    @Setter
    @Column(name = "user_edu")
    private byte userEdu;
    @Setter
    private byte isActive;

    private String password;
}
