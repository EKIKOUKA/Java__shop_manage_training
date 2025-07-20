package com.shop.sample.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequest {
    private Integer current = 1;
    private Integer pageSize = 10;
    private String username;
    private String gender;
    private Integer userEdu;
    private String userEmail;
    private Integer isActive = 1;
}
