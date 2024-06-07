package com.kttkpm.FoodOrder.Payload.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kttkpm.FoodOrder.Entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private int id;
    private Boolean isPayed;
    private String paymentStatus;
    private Date paymentDate;
    private int orderId;
    private String transactionId;
    private String paymentAmount;
}