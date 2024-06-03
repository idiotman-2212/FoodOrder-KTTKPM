package com.kttkpm.FoodOrder.Payload.Request;

import com.kttkpm.FoodOrder.Entity.ProductEntity;
import com.kttkpm.FoodOrder.Entity.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private UserEntity user;
    private List<ProductEntity> products;
    private Double orderFee;
    private String orderDesc;
    private String address;
}
