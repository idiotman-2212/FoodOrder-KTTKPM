package com.kttkpm.FoodOrder.Payload.Request;

import com.kttkpm.FoodOrder.Entity.ProductEntity;
import com.kttkpm.FoodOrder.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private UserEntity user;
    private List<ProductEntity> products;
    private Double orderFee;
    private String orderDesc;
    private String address;
    private Date orderDate;
}
