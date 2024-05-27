package com.kttkpm.FoodOrder.Controller;

import com.kttkpm.FoodOrder.Entity.ProductEntity;
import com.kttkpm.FoodOrder.Helper.GlobalData;
import com.kttkpm.FoodOrder.Helper.ProductConverter;
import com.kttkpm.FoodOrder.Payload.Response.ProductResponse;
import com.kttkpm.FoodOrder.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductConverter convertToProductEntity;

    @GetMapping("/cart")
    public String cartGet(Model model){
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total",  GlobalData.cart.stream().mapToDouble(ProductEntity::getPrice).sum());
        model.addAttribute("cart", GlobalData.cart);
        return "cart";
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable int id){
        ProductResponse productResponse = productService.getProductById(id);
        if (productResponse != null) {
            ProductEntity product = convertToProductEntity(productResponse);
            GlobalData.cart.add(product);
        } else {
            // Handle product not found error
        }
        return "redirect:/shop";
    }

    private ProductEntity convertToProductEntity(ProductResponse productResponse) {
        ProductEntity productEntity = new ProductEntity();
        // Set properties of productEntity from productResponse
        return productEntity;
    }

    @GetMapping("/cart/removeItem/{index}")
    public String cartItemRemove(@PathVariable int index){
        if (index >= 0 && index < GlobalData.cart.size()) {
            GlobalData.cart.remove(index);
        }
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model){
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total",  GlobalData.cart.stream().mapToDouble(ProductEntity::getPrice).sum());
        return "checkout";
    }
}
