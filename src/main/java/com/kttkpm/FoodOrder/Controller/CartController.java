package com.kttkpm.FoodOrder.Controller;

import com.kttkpm.FoodOrder.Entity.CartEntity;
import com.kttkpm.FoodOrder.Entity.ProductEntity;
import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Helper.GlobalData;
import com.kttkpm.FoodOrder.Payload.Request.CartRequest;
import com.kttkpm.FoodOrder.Payload.Response.CartResponse;
import com.kttkpm.FoodOrder.Service.CartService;
import com.kttkpm.FoodOrder.Service.ProductService;
import com.kttkpm.FoodOrder.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    @GetMapping("")
    public String viewCart(Model model) {
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity userId = userService.findUserByEmail(currentUser);
        List<CartResponse> cartItems = cartService.getCartByUserId(userId.getId());
        int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();
        double total = cartItems.stream().mapToDouble(item -> item.getTotalCostProduct()).sum();

        model.addAttribute("cartCount", cartCount);
        model.addAttribute("cart", cartItems);
        model.addAttribute("total", total);

        return "cart";
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserEntity user = userService.findUserByEmail(currentPrincipalName);

        if (user != null) {
            CartRequest request = new CartRequest();
            request.setUserId(user.getId());
            request.setProductId(id);
            request.setQuantity(1);

            boolean response = cartService.insertProductIntoCart(request);
            model.addAttribute("cart", response);
        } else {
            model.addAttribute("errorMessage", "User not found");
        }

        return "redirect:/shop";
    }

    @GetMapping("/removeItem/{id}")
    public String removeItem(@PathVariable("id") int cartItemId) {
        cartService.deleteCartById(cartItemId);
        return "redirect:/cart";
    }

}
