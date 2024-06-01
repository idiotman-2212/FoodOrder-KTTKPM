package com.kttkpm.FoodOrder.Service;

import com.kttkpm.FoodOrder.Entity.CartEntity;
import com.kttkpm.FoodOrder.Helper.CartConverter;
import com.kttkpm.FoodOrder.Payload.Request.CartRequest;
import com.kttkpm.FoodOrder.Payload.Response.CartResponse;
import com.kttkpm.FoodOrder.Payload.Response.UserResponse;
import com.kttkpm.FoodOrder.Repository.CartRepository;
import com.kttkpm.FoodOrder.Service.Imp.CartServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class CartService implements CartServiceImp {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartConverter cartConverter;

    @Override
    public List<CartResponse> getAllCart() {
        List<CartEntity> cartEntities = cartRepository.findAll();
        List<CartResponse> cartResponses = new ArrayList<>();

        for (CartEntity cartEntity : cartEntities) {
            CartResponse cartResponse = new CartResponse().builder()
                    .id(cartEntity.getId())
                    .userId(cartEntity.getUser().getId())

                    .build();


            cartResponses.add(cartResponse);
        }

        return cartResponses;
    }
    @Override
    public CartResponse getCartById(int cartId) {
        Optional<CartEntity> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            CartEntity cartEntity = optionalCart.get();
            CartResponse cartResponse = new CartResponse().builder()
                    .id(cartEntity.getId())
                    .userId(cartEntity.getUser().getId())
                    .build();

            return cartResponse;
        } else {
            throw new RuntimeException("Cart not found with id: " + cartId);
        }
    }

    @Override
    public boolean insertCart(CartRequest cartRequest) {
        CartEntity cartEntity = CartConverter.toCartEntity(cartRequest);
        //cartEntity.setUserId(cartRequest.getUserId());

        cartRepository.save(cartEntity);
        return true;
    }

    @Override
    public boolean updateCartById(int cartId, int userId) {
        Optional<CartEntity> optionalCart = cartRepository.findById(cartId);
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        log.info(auth.getName());
        if(optionalCart.isPresent()){
            CartEntity existingCart = optionalCart.get();
            //existingCart.setUserId(userId);
            cartRepository.save(existingCart);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean deleteCartById(int cartId) {
        Optional<CartEntity> optionalCart = cartRepository.findById(cartId);
        if(optionalCart.isPresent()){
            cartRepository.deleteById(cartId);
            return true;
        }else{
            return false;
        }
    }
}