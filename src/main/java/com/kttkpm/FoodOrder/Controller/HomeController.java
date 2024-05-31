package com.kttkpm.FoodOrder.Controller;

import com.kttkpm.FoodOrder.Entity.RoleEntity;
import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Helper.GlobalData;
import com.kttkpm.FoodOrder.Payload.Request.SignUpRequest;
import com.kttkpm.FoodOrder.Payload.Response.ProductResponse;
import com.kttkpm.FoodOrder.Payload.Response.UserResponse;
import com.kttkpm.FoodOrder.Repository.RoleRepository;
import com.kttkpm.FoodOrder.Repository.UserRepository;
import com.kttkpm.FoodOrder.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    private VNPayService vnPayService;

    @GetMapping({"/", "/home"})
    public String home(Model model){
        model.addAttribute("cartCount", GlobalData.cart.size());
        return "index";
    } //index
    @GetMapping("/users/add")
    public String updateUser(Model model){
        UserResponse currentUser = new UserResponse();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails && ((UserDetails) principal).getUsername() != null) {
            String currentUsername = ((UserDetails)principal).getUsername();
            UserEntity user = userService.findUserByEmail(currentUsername);
            currentUser.setId(user.getId());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword("");
            currentUser.setPhone(user.getPhone());
            currentUser.setAddress(user.getAddress());
            List<Integer> roleIds = new ArrayList<>();

            currentUser.setRoles(String.valueOf(user));
        }//get current User runtime
        model.addAttribute("userDTO", currentUser);
        return "userRoleAdd";
    }

    @PostMapping("/users/add")
    public String postUserAdd(@ModelAttribute("userDTO") SignUpRequest signUpRequest, Model model) {
        if (userRepository.findByEmail(signUpRequest.getEmail()) != null) {
            model.addAttribute("error", "Email đã tồn tại");
            return "userRoleAdd";
        }

        UserEntity user = new UserEntity();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setPhone(signUpRequest.getPhone());
        user.setAddress(signUpRequest.getAddress());

        RoleEntity role = roleRepository.findById(signUpRequest.getIdRole()).orElse(null);
        if (role != null) {
            RoleEntity roles = new RoleEntity();
            user.setRole(roles);
        } else {
            model.addAttribute("error", "Vai trò không tồn tại");
            return "userRoleAdd";
        }

        userRepository.save(user);

        return "redirect:/index";
    }

    @GetMapping("/shop")
    public String shop(Model model, @RequestParam(name = "pageNo", defaultValue ="1") Integer pageNo ){

        Page<ProductResponse> products = productService.getAllProductsPage(pageNo);
        if(pageNo != null){
            model.addAttribute("totalPage", products.getTotalPages());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("cartCount", GlobalData.cart.size());
            model.addAttribute("categories", categoryService.getAllCategory());
            model.addAttribute("products", products.getContent());
        }
        return "shop";
    }
    //xem tất cả sản phầm và phân trang

    @GetMapping("/shop/category/{id}")
    public String shopByCat(@PathVariable int id, Model model){
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductByCategoryId(id));
        return "shop";
    } //view Products By Category

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(@PathVariable int id, Model model){
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("product", productService.getProductById(id));
        return "viewProduct";
    } //view product Details

    @GetMapping("/blog")
    public String blog(Model model){
        return "blog";
    }// view blog

    @GetMapping("/blog-detail")
    public String blogDetail(Model model){
        return "blog-detail";
    }// view blog detail

    @GetMapping("/about")
    public String about(Model model){
        return "about";
    }// view about

    @GetMapping("/contact")
    public String contact(Model model){
        return "contact";
    }// view contact

    @GetMapping("/payNow")
    public String paynow(Model model){
        return "orderPlaced";
    }// view order

    @GetMapping("/vnpay")
    public String home(){
        return "createOrder";
    }//thanh toán vnpay

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model){
        int paymentStatus =vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }

}
