package com.kttkpm.FoodOrder.Payload.Response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.kttkpm.FoodOrder.Entity.OrderStatus;
import com.kttkpm.FoodOrder.Entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private int id;
    private int userId;
    private String username;
    private Date orderDate;
    private String orderDesc;
    private Double orderFee;
    private OrderStatus orderStatus;
    private List<ProductResponse> products;
}
