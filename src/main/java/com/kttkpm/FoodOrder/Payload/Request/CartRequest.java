package com.kttkpm.FoodOrder.Payload.Request;

import lombok.Data;

@Data
public class CartRequest {
    private int userId;
    private int quantity;
    private int productId;

}