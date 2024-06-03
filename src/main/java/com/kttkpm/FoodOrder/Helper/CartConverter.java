package com.kttkpm.FoodOrder.Helper;

import com.kttkpm.FoodOrder.Entity.CartEntity;
import com.kttkpm.FoodOrder.Payload.Response.CartResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartConverter {
    private OrderConverter orderConverter;

    public static CartResponse toCartResponse(CartEntity cartEntity) {
        if (cartEntity == null) {
            return null;
        }

        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(cartEntity.getId());
        cartResponse.setUserName(cartEntity.getUser().getUsername());
        cartResponse.setProduct(cartEntity.getProduct());
        // Chuyển đổi tập hợp các OrderEntity thành tập hợp các OrderResponse
//        cartResponse.setOrderResponseSet(
//                cartEntity.getOrders().stream()
//                        .map(orderConverter.toOrderResponse()) // Sử dụng converter của OrderEntity
//                        .collect(Collectors.toSet())
//        );

        return cartResponse;
    }

    public static CartEntity toCartEntity(CartResponse cartResponse) {
        if (cartResponse == null) {
            return null;
        }

        CartEntity cartEntity = new CartEntity();
        cartEntity.setId(cartResponse.getId());
//        cartEntity.setUser(cartResponse.getUserId());
//        // Chuyển đổi tập hợp các OrderResponse thành tập hợp các OrderEntity
//        cartEntity.setOrders(
//                cartResponse.getOrderResponseSet().stream()
//                        .map(OrderConverter::toOrderEntity) // Sử dụng converter của OrderResponse
//                        .collect(Collectors.toSet())
//        );

        return cartEntity;
    }
}

