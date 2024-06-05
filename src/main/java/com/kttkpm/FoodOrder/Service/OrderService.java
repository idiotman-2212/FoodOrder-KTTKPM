package com.kttkpm.FoodOrder.Service;

import com.kttkpm.FoodOrder.Entity.*;
import com.kttkpm.FoodOrder.Payload.Request.OrderRequest;
import com.kttkpm.FoodOrder.Payload.Response.CategoryResponse;
import com.kttkpm.FoodOrder.Payload.Response.OrderResponse;
import com.kttkpm.FoodOrder.Payload.Response.ProductResponse;
import com.kttkpm.FoodOrder.Payload.Response.UserResponse;
import com.kttkpm.FoodOrder.Repository.CartRepository;
import com.kttkpm.FoodOrder.Repository.OrderRepository;
import com.kttkpm.FoodOrder.Repository.UserRepository;
import com.kttkpm.FoodOrder.Service.Imp.OrderServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService implements OrderServiceImp {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public OrderResponse getOrderById(int id) {
        Optional<OrderEntity> order = orderRepository.findById(id);
        if (order.isPresent()) {
            OrderEntity o = order.get();
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(o.getId());
            orderResponse.setOrderDate(o.getOrderDate());
            orderResponse.setUserId(o.getUser().getId());
            orderResponse.setUsername(o.getUser().getUsername());
            orderResponse.setOrderDesc(o.getOrderDesc());
            orderResponse.setOrderStatus(o.getOrderStatus());
            orderResponse.setOrderFee(o.getOrderFee());
            return orderResponse;
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    @Override
    public List<OrderResponse> getAllOrder() {
        List<OrderEntity> list = orderRepository.findAll();
        List<OrderResponse> responseList = new ArrayList<>();

        for (OrderEntity o : list) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(o.getId());
            orderResponse.setOrderDate(o.getOrderDate());
            orderResponse.setUsername(o.getUser().getUsername());
            orderResponse.setOrderDesc(o.getOrderDesc());
            orderResponse.setOrderStatus(o.getOrderStatus());
            orderResponse.setOrderFee(o.getOrderFee());
            List<ProductResponse> productResponses = new ArrayList<>();
            for (ProductEntity product : o.getProducts()) {
                ProductResponse productResponse = new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getImage(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getQuantity(),
                        product.getCreateDate(),
                        new CategoryResponse(product.getCategory().getId(), product.getCategory().getName())
                );
                productResponses.add(productResponse);
            }
            orderResponse.setProducts(productResponses);

            responseList.add(orderResponse);
        }

        return responseList;
    }

    public OrderResponse placeOrder(OrderRequest request) {
        UserEntity user = userRepository.findById(request.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
        List<CartEntity> cartItems = (List<CartEntity>) cartRepository.findByUserId(request.getUser().getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setOrderStatus(OrderStatus.PENDING); // Setting default order status to PENDING
        order.setOrderDesc(request.getOrderDesc());
        order.setProducts(cartItems.stream().map(CartEntity::getProduct).collect(Collectors.toList()));
        order.setOrderFee(cartItems.stream().mapToDouble(cart -> cart.getProduct().getPrice() * cart.getQuantity()).sum());
        OrderEntity savedOrder = orderRepository.save(order);

        cartRepository.deleteAll(cartItems);
        
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getUser().getId(),
                savedOrder.getUser().getUsername(),
                savedOrder.getOrderDate(),
                savedOrder.getOrderDesc(),
                savedOrder.getOrderFee(),
                savedOrder.getOrderStatus(), // Add order status to response
                savedOrder.getProducts().stream().map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getImage(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getQuantity(),
                        product.getCreateDate(),
                        new CategoryResponse(product.getCategory().getId(), product.getCategory().getName())
                )).collect(Collectors.toList())
        );
    }

    @Override
    public boolean saveOrder(OrderEntity orderEntity) {
        try {
            orderRepository.save(orderEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteOrderById(int idOrder) {
        try {
            orderRepository.deleteById(idOrder);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<OrderResponse> findOrderDetailByOrderId(int idOrder) {
        Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(idOrder);
        List<OrderResponse> orderResponses = new ArrayList<>();
        if (optionalOrderEntity.isPresent()) {
            OrderEntity orderEntity = optionalOrderEntity.get();
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(orderEntity.getId());
            orderResponse.setOrderDate(orderEntity.getOrderDate());
            orderResponse.setUserId(orderEntity.getUser().getId());
            orderResponse.setOrderDesc(orderEntity.getOrderDesc());

            List<ProductResponse> productResponses = new ArrayList<>();
            for (ProductEntity productEntity : orderEntity.getProducts()) {
                ProductResponse productResponse = new ProductResponse(
                        productEntity.getId(),
                        productEntity.getName(),
                        productEntity.getImage(),
                        productEntity.getPrice(),
                        productEntity.getDescription(),
                        productEntity.getQuantity(),
                        productEntity.getCreateDate(),
                        new CategoryResponse(productEntity.getCategory().getId(), productEntity.getCategory().getName())
                );
                productResponses.add(productResponse);
            }
            orderResponse.setProducts(productResponses);

            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Override
    public List<OrderResponse> getOrderByIdUser(int idUser) {
        List<OrderEntity> orderEntities = orderRepository.findByUserId(idUser);
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(orderEntity.getId());
            orderResponse.setOrderDate(orderEntity.getOrderDate());
            orderResponse.setUserId(orderEntity.getUser().getId());
            orderResponse.setOrderStatus(orderEntity.getOrderStatus());
            orderResponse.setOrderDesc(orderEntity.getOrderDesc());
            orderResponse.setOrderFee(orderEntity.getOrderFee());

            List<ProductResponse> productResponses = new ArrayList<>();
            for (ProductEntity productEntity : orderEntity.getProducts()) {
                ProductResponse productResponse = new ProductResponse(
                        productEntity.getId(),
                        productEntity.getName(),
                        productEntity.getImage(),
                        productEntity.getPrice(),
                        productEntity.getDescription(),
                        productEntity.getQuantity(),
                        productEntity.getCreateDate(),
                        new CategoryResponse(productEntity.getCategory().getId(), productEntity.getCategory().getName())
                );
                productResponses.add(productResponse);
            }
            orderResponse.setProducts(productResponses);

            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Override
    public void placeOrder(int userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<CartEntity> cartItems = (List<CartEntity>) cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setProducts(cartItems.stream().map(CartEntity::getProduct).collect(Collectors.toList()));
        order.setOrderFee(cartItems.stream().mapToDouble(cart -> cart.getProduct().getPrice() * cart.getQuantity()).sum());
        orderRepository.save(order);

        // Clear the cart
        cartRepository.deleteAll(cartItems);
    }

    public void updateOrderStatus(int id, OrderStatus orderStatus) {
        Optional<OrderEntity> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            OrderEntity order = orderOptional.get();
            order.setOrderStatus(orderStatus);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    public List<OrderResponse> searchOrders(String keyword) {
        List<OrderEntity> order = orderRepository.searchUsers(keyword);
        List<OrderResponse> list = new ArrayList<>();
        for (OrderEntity u: order) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(u.getId());
            orderResponse.setUsername(u.getUser().getUsername());
            orderResponse.setOrderDate(u.getOrderDate());
            orderResponse.setOrderFee(u.getOrderFee());
            orderResponse.setOrderStatus(u.getOrderStatus());

            list.add(orderResponse);
        }
        return list;
    }
    public Page<OrderResponse> getAllOrdersPage(Integer pageNo) {
        int pageSize = 5; // Số sản phẩm trên mỗi trang
        List<OrderResponse> allOrder = getAllOrder();
        // Phân trang dữ liệu
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allOrder.size());

        List<OrderResponse> sublist = allOrder.subList(start, end);
        return new PageImpl<>(sublist, pageable, allOrder.size());
    }

    public OrderResponse updateOrderDesc(int userId, String orderDesc) {
        // Fetch the latest order for the user to update the description
        OrderEntity order = orderRepository.findTopByUserIdOrderByOrderDateDesc(userId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderDesc(orderDesc);
        OrderEntity updatedOrder = orderRepository.save(order);
        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getUser().getUsername(),
                order.getOrderDate(),
                order.getOrderDesc(),
                order.getOrderFee(),
                order.getOrderStatus(), // Add order status to response
                order.getProducts().stream().map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getImage(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getQuantity(),
                        product.getCreateDate(),
                        new CategoryResponse(product.getCategory().getId(), product.getCategory().getName())
                )).collect(Collectors.toList())
        );
    }
}