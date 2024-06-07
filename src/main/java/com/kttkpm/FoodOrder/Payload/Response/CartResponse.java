package com.kttkpm.FoodOrder.Payload.Response;

import com.kttkpm.FoodOrder.Entity.OrderEntity;
import com.kttkpm.FoodOrder.Entity.ProductEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
@Data
public class CartResponse implements Serializable {
    private int id;
    private String userName;
    private ProductEntity product;
    private int quantity;
    private List<OrderEntity> orders;
    private double totalCostProduct;
    public CartResponse() {
    }

    public CartResponse(int id, String userName, ProductEntity product, int quantity, List<OrderEntity> orders, double totalCostProduct) {
        this.id = id;
        this.userName = userName;
        this.product = product;
        this.quantity = quantity;
        this.orders = orders;
        this.totalCostProduct = totalCostProduct;
    }
}