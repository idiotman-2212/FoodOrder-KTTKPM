package com.kttkpm.FoodOrder.Service;

import com.kttkpm.FoodOrder.Entity.CartEntity;
import com.kttkpm.FoodOrder.Entity.ProductEntity;
import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Helper.CartConverter;
import com.kttkpm.FoodOrder.Payload.Request.CartRequest;
import com.kttkpm.FoodOrder.Payload.Response.CartResponse;
import com.kttkpm.FoodOrder.Payload.Response.UserResponse;
import com.kttkpm.FoodOrder.Repository.CartRepository;
import com.kttkpm.FoodOrder.Repository.ProductRepository;
import com.kttkpm.FoodOrder.Repository.UserRepository;
import com.kttkpm.FoodOrder.Service.Imp.CartServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService implements CartServiceImp {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public boolean insertProductIntoCart(CartRequest cartRequest) {

        Optional<ProductEntity> product = productRepository.findById(cartRequest.getProductId());
        Optional<UserEntity> user = userRepository.findById(cartRequest.getUserId());

        if(product.isPresent() && user.isPresent()){

            CartEntity cart;
            // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng của người dùng hay chưa
            Optional<CartEntity> existingCartItem = cartRepository.findByUserIdAndProductId(user.get().getId(), product.get().getId());

            if (existingCartItem.isPresent()) {
                cart = existingCartItem.get();
                cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());

            }else {
                cart = new CartEntity();
                cart.setProduct(product.get());
                cart.setUser(user.get());
                cart.setQuantity(cartRequest.getQuantity());
            }

            try {
                cartRepository.save(cart);
                return true;

            } catch (Exception e){
                System.out.printf("Error: " + e);
                return false;
            }
        }

        return false;
    }

    @Override
    public List<CartResponse> getCartByUserId(int userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        List<CartEntity> cartEntities = cartRepository.findByUserId(userId);
        List<CartResponse> cartResponses = new ArrayList<>();

        for (CartEntity c : cartEntities) {
            CartResponse cartTemp = new CartResponse();
            cartTemp.setId(c.getId());
            cartTemp.setQuantity(c.getQuantity());
            cartTemp.setProduct(c.getProduct());
            cartTemp.setUserName(c.getUser().getUsername());

            // Tính tổng tiền cho từng sản phẩm trong giỏ hàng
            cartTemp.setTotalCostProduct(c.getProduct().getPrice() * c.getQuantity());
            cartResponses.add(cartTemp);
        }

        return cartResponses;
    }



    @Override
    public void clearCartByUserId(int userId) {
        List<CartEntity> cartItems = (List<CartEntity>) cartRepository.findByUserId(userId);
        cartRepository.deleteAll(cartItems);
    }

    @Override
    public boolean updateCartById(int cartId, int userId) {
        Optional<CartEntity> optionalCart = cartRepository.findById(cartId);
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