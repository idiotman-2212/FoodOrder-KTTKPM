package com.kttkpm.FoodOrder.Payload.Response;

<<<<<<< HEAD
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
=======
import lombok.*;

import java.io.Serializable;
import java.util.Set;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartResponse implements Serializable {
    private int id;
    private int userId;
    private Set<OrderResponse> orderResponseSet;
    private UserResponse userResponse;


//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public Set<OrderResponse> getOrderResponseSet() {
//        return orderResponseSet;
//    }
//
//    public void setOrderResponseSet(Set<OrderResponse> orderResponseSet) {
//        this.orderResponseSet = orderResponseSet;
//    }
//
//    public UserResponse getUserResponse() {
//        return userResponse;
//    }
//
//    public void setUserResponse(UserResponse userResponse) {
//        this.userResponse = userResponse;
//    }
>>>>>>> origin/tai-dev
}