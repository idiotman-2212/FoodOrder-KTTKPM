package com.kttkpm.FoodOrder.Payload.Response;

import lombok.*;

import java.util.Date;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleResponse {
    private int id;
    private String name;
    private Date createDate;



}