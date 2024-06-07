package com.kttkpm.FoodOrder.Payload.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Boolean isPayed;
    private String paymentStatus;
    private Date paymentDate;
    private int orderId;
    private String transactionId;
    private String paymentAmount;
}
