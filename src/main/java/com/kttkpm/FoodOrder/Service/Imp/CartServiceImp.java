package com.kttkpm.FoodOrder.Service.Imp;

import com.kttkpm.FoodOrder.Payload.Request.CartRequest;
import com.kttkpm.FoodOrder.Payload.Response.CartResponse;

import java.util.List;

public interface CartServiceImp {
    boolean insertProductIntoCart(CartRequest cartRequest);
    boolean updateCartById(int cartId,int userId);
    boolean deleteCartById(int cartId);
    List<CartResponse> getCartByUserId(int userId);
    void clearCartByUserId(int userId);

}
