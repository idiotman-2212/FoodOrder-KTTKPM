package com.kttkpm.FoodOrder.Service.Imp;

import com.kttkpm.FoodOrder.Entity.OrderEntity;
import com.kttkpm.FoodOrder.Payload.Response.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderServiceImp {
    OrderResponse getOrderById(int id);


    List<OrderResponse> getAllOrder() ;

    boolean saveOrder(OrderEntity orderEntity);

    boolean deleteOrderById(int idOrder);

    List<OrderResponse> findOrderDetailByOrderId(int idOrder);

    List<OrderResponse> getOrderByIdUser(int idUser);

    void placeOrder(int userId);

    Page<OrderResponse> getAllPageOrderByIdUser(Integer pageNo, Integer id);
}
