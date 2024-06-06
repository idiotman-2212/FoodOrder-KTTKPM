package com.kttkpm.FoodOrder.Payload.Response;

import lombok.*;

import java.util.Date;
import java.util.Set;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    private int id;
    private String username;
    private String email;
    private Date createDate;
    private  String password;
    private String phone;
    private String address;
    private String roles;


}