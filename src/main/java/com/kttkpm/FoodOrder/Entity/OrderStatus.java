package com.kttkpm.FoodOrder.Entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum OrderStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    CANCELED;
    private String status;
    public String getStatus(){
        return status;
    }
}
