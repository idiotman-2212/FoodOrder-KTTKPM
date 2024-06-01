package com.kttkpm.FoodOrder.Helper;

import com.kttkpm.FoodOrder.Entity.CartEntity;
import com.kttkpm.FoodOrder.Payload.Request.CartRequest;
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
        cartResponse.setUserId(cartEntity.getUser().getId());
        // Chuyển đổi tập hợp các OrderEntity thành tập hợp các OrderResponse
//        cartResponse.setOrderResponseSet(
//                cartEntity.getOrders().stream()
//                        .map(orderConverter.toOrderResponse()) // Sử dụng converter của OrderEntity
//                        .collect(Collectors.toSet())
//        );

        return cartResponse;
    }

    public static CartEntity toCartEntity(CartRequest cartRequest) {
        if (cartRequest == null) {
            return null;
        }

        CartEntity cartEntity = new CartEntity();
        cartEntity.setId(cartRequest.getId());


        return cartEntity;
    }
}

