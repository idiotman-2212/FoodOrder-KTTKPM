package com.kttkpm.FoodOrder.Controller;

import com.kttkpm.FoodOrder.Entity.RoleEntity;
import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Helper.GlobalData;
import com.kttkpm.FoodOrder.Payload.Request.SignUpRequest;
import com.kttkpm.FoodOrder.Payload.Response.CartResponse;
import com.kttkpm.FoodOrder.Payload.Response.ProductResponse;
import com.kttkpm.FoodOrder.Payload.Response.UserResponse;
import com.kttkpm.FoodOrder.Repository.RoleRepository;
import com.kttkpm.FoodOrder.Repository.UserRepository;
import com.kttkpm.FoodOrder.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@Controller
public class HomeController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository  roleRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @GetMapping({"/", "/home"})
    public String home(Model model){
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);
        if(user != null){
            model.addAttribute("username", user.getUsername());
        }else {
            model.addAttribute("username", "Guest");
        }
        List<CartResponse> cartItems = cartService.getCartByUserId(user.getId());
        int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();
        model.addAttribute("cartCount", cartCount);
        return "index";
    } //index

    @GetMapping("/users")
    public String welcome(Model model) {
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);
        List<CartResponse> cartItems = cartService.getCartByUserId(user.getId());
        int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("username", user.getUsername());
        return "users";
    }//show user page

    @GetMapping("/users/add")
    public String updateUser(Model model) {
        UserResponse currentUser = new UserResponse();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String currentUsername = ((UserDetails) principal).getUsername();
            UserEntity user = userService.findUserByEmail(currentUsername);
            if (user != null) {
                currentUser.setId(user.getId());
                currentUser.setUsername(user.getUsername());
                currentUser.setEmail(user.getEmail());
                currentUser.setPassword("");
                currentUser.setPhone(user.getPhone());
                currentUser.setAddress(user.getAddress());
            } else {
                model.addAttribute("error", "Người dùng không tồn tại");
                return "403";
            }
        }
        List<CartResponse> cartItems = cartService.getCartByUserId(currentUser.getId());
        int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();

        model.addAttribute("cartCount", cartCount);
        model.addAttribute("userDTO", currentUser);
        return "userRoleAdd";
    }

    @PostMapping("/users/add")
    public String postUserEdit(@ModelAttribute("userDTO") UserResponse userResponse, Model model) {
        // Tìm người dùng hiện tại
        String currentUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUsername);
        if (user != null) {
            user.setUsername(userResponse.getUsername());
            user.setEmail(userResponse.getEmail());
            user.setPhone(userResponse.getPhone());
            user.setAddress(userResponse.getAddress());
            user.setPassword(passwordEncoder.encode(userResponse.getPassword()));
            userRepository.save(user);
            return "redirect:/users";
        } else {
            model.addAttribute("error", "Người dùng không tồn tại");
            return "403";
        }
    }


    @GetMapping("/shop")
    public String shop(Model model, @RequestParam(name = "pageNo", defaultValue ="1") Integer pageNo ){

        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);

        List<CartResponse> cartItems = cartService.getCartByUserId(user.getId());
        int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();//model.addAttribute("cartCount", cartCount);
        Page<ProductResponse> products = productService.getAllProductsPage(pageNo);
        if(pageNo != null){
            model.addAttribute("totalPage", products.getTotalPages());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("cartCount", cartCount);
            model.addAttribute("categories", categoryService.getAllCategory());
            model.addAttribute("products", products.getContent());
        }
        return "shop";
    }
    //xem tất cả sản phầm và phân trang

    @GetMapping("/shop/category/{id}")
    public String shopByCat(@PathVariable int id, Model model){
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);

        List<CartResponse> cartItems = cartService.getCartByUserId(user.getId());
        int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductByCategoryId(id));
        return "shop";
    } //view Products By Category

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(@PathVariable int id, Model model){
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);

        List<CartResponse> cartItems = cartService.getCartByUserId(user.getId());
        int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("product", productService.getProductById(id));
        return "viewProduct";
    } //view product Details

    @GetMapping("/403")
    public String error(Model model){
        return "403";
    }// 403

    @GetMapping("/blog")
    public String blog(Model model){
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);

        List<CartResponse> cartItems = cartService.getCartByUserId(user.getId());
        int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();
        model.addAttribute("cartCount", cartCount);
        return "blog";
    }// view blog

    @GetMapping("/blog-detail")
    public String blogDetail(Model model){
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);

        List<CartResponse> cartItems = cartService.getCartByUserId(user.getId());
        int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();
        model.addAttribute("cartCount", cartCount);
        return "blog-detail";
    }// view blog detail

    @GetMapping("/about")
    public String about(Model model){
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);

        List<CartResponse> cartItems = cartService.getCartByUserId(user.getId());
        int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();
        model.addAttribute("cartCount", cartCount);
        return "about";
    }// view about

    @GetMapping("/contact")
    public String contact(Model model){
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);

        List<CartResponse> cartItems = cartService.getCartByUserId(user.getId());
        int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();
        model.addAttribute("cartCount", cartCount);
        return "contact";
    }// view contact

}
