package com.kttkpm.FoodOrder.Payload.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kttkpm.FoodOrder.Entity.PaymentStatus;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentResponse {
    private int id;
    private Boolean isPayed;
    private PaymentStatus paymentStatus;

    private int orderId;
    private int userId;
    @JsonProperty("order")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderResponse orderResponse;


}