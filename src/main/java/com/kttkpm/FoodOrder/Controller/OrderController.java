package com.kttkpm.FoodOrder.Controller;

import com.kttkpm.FoodOrder.Entity.UserEntity;
import com.kttkpm.FoodOrder.Payload.Request.OrderRequest;
import com.kttkpm.FoodOrder.Payload.Request.PaymentRequest;
import com.kttkpm.FoodOrder.Payload.Response.CartResponse;
import com.kttkpm.FoodOrder.Payload.Response.OrderResponse;
import com.kttkpm.FoodOrder.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;
    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model){
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);

        List<CartResponse> cartItems = cartService.getCartByUserId(user.getId());
        int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();
        double total = cartItems.stream().mapToDouble(item -> item.getTotalCostProduct()).sum();

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());
        model.addAttribute("address", user.getAddress());
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("total", total);
        model.addAttribute("orderRequest", new OrderRequest());
        return "checkout";
    }

    @PostMapping("/checkout/order")
    public String placeOrder(@ModelAttribute OrderRequest request, Model model) {
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);
        request.setUser(user);

        OrderResponse response = orderService.placeOrder(request);
        model.addAttribute("user", user.getUsername());
        model.addAttribute("order", response);
        return "order";
    }

    @PostMapping("/checkout/submitOrder")
    public String submitOrder(@ModelAttribute OrderRequest request, Model model){
        String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity user = userService.findUserByEmail(currentUser);
        request.setUser(user);

/*        List<OrderResponse> orderResponses = orderService.getOrderByIdUser(user.getId());
        OrderResponse response = orderResponses.isEmpty() ? null : orderResponses.get(0);*/
        OrderResponse response = orderService.updateOrderDesc(user.getId(), request.getOrderDesc());
        model.addAttribute("user", user.getUsername());
        model.addAttribute("order", response);
        return "orderPlaced";
    }

    //Thanh toan thong qua VN-Pay
   @PostMapping("/checkout/paynow")
    public String paynowVNPay(@ModelAttribute OrderRequest request, Model model){
       String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
       UserEntity user = userService.findUserByEmail(currentUser);
       request.setUser(user);

       List<CartResponse> cartItems = cartService.getCartByUserId(user.getId());
       int cartCount = cartItems.stream().mapToInt(CartResponse::getQuantity).sum();
       double total = cartItems.stream().mapToDouble(item -> item.getTotalCostProduct()).sum();

       OrderResponse response = orderService.placeOrder(request);
       model.addAttribute("user", user.getUsername());
       model.addAttribute("cartCount", cartCount);
       model.addAttribute("order", response);
       return "createOrder";
    }

    // Chuyển hướng người dùng đến cổng thanh toán VNPay
    @PostMapping("/checkout/submitOrderVnpay")
    public String submitOrder(@Valid @ModelAttribute("order") OrderRequest orderRequest, BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            return "createOrder"; // Return to form if validation errors are present
        }

        if (orderRequest.getOrderFee() == null) {
            model.addAttribute("error", "Order fee is required.");
            return "createOrder"; // Return to the form page if there's an error
        }

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderRequest, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPay sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        // Save payment information to database
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setIsPayed(paymentStatus == 1);
        paymentRequest.setPaymentStatus(paymentStatus == 1 ? "COMPLETED" : "IN_PROGRESS");
        paymentRequest.setOrderId(Integer.parseInt(orderInfo));
        paymentRequest.setTransactionId(transactionId);
        paymentRequest.setPaymentAmount(totalPrice);

        paymentService.createPayment(paymentRequest);

        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }

    @GetMapping("/viewOrderHistory")
        public String viewOrderHistory(Model model, @RequestParam(name = "pageNo", defaultValue ="1") Integer pageNo) {
            String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity user = userService.findUserByEmail(currentUser);
            Page<OrderResponse> orderRe = orderService.getAllPageOrderByIdUser(pageNo,user.getId());
            model.addAttribute("orders", orderRe);
            model.addAttribute("totalPage", orderRe.getTotalPages());
            model.addAttribute("currentPage", pageNo); // Trang hiện tại

            model.addAttribute("user", user);
            model.addAttribute("orders", orderRe);


            return "orderHistory";
        }
}
