package com.kttkpm.FoodOrder.Controller;

import com.kttkpm.FoodOrder.Payload.Response.BaseResponse;
import com.kttkpm.FoodOrder.Payload.Response.CartResponse;
import com.kttkpm.FoodOrder.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
public class api {
    @Autowired
    private CartService cartService;

    @GetMapping("/carts/{userId}")
    public ResponseEntity<?> getCarts(@PathVariable int userId){

        List<CartResponse> cartResponseList = cartService.getCartByUserId(userId);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage("Get cart info by username");
        baseResponse.setData(cartResponseList);
        baseResponse.setStatusCode(200);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

}
