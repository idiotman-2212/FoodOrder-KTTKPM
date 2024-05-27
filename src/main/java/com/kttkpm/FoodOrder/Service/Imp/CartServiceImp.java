package com.kttkpm.FoodOrder.Service.Imp;

import com.kttkpm.FoodOrder.Payload.Request.CartRequest;
import com.kttkpm.FoodOrder.Payload.Response.CartResponse;

import java.util.List;

public interface CartServiceImp {
    List<CartResponse> getAllCart();
    CartResponse getCartById(int cartId);
    boolean insertCart(CartRequest cartRequest);
    boolean updateCartById(int cartId,int userId);
    boolean deleteCartById(int cartId);
}
