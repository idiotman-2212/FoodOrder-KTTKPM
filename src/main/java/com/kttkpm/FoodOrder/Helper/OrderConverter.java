package com.kttkpm.FoodOrder.Helper;

import com.kttkpm.FoodOrder.Entity.OrderEntity;
import com.kttkpm.FoodOrder.Payload.Response.OrderResponse;
import org.springframework.stereotype.Component;
@Component
public class OrderConverter {

    public OrderResponse toOrderResponse(OrderEntity orderEntity) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(orderEntity.getId());
        orderResponse.setOrderDate(orderEntity.getOrderDate());
        orderResponse.setOrderDesc(orderEntity.getOrderDesc());
        orderResponse.setOrderFee(orderEntity.getOrderFee());
        return orderResponse;
    }

    public OrderEntity toOrderEntity(OrderResponse orderResponse) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderResponse.getId());
        orderEntity.setOrderDate(orderResponse.getOrderDate());
        orderEntity.setOrderDesc(orderResponse.getOrderDesc());
        orderEntity.setOrderFee(orderResponse.getOrderFee());
        return orderEntity;
    }

}