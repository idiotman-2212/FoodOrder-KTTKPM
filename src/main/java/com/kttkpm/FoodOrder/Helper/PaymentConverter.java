package com.kttkpm.FoodOrder.Helper;

import com.kttkpm.FoodOrder.Entity.PaymentEntity;
import com.kttkpm.FoodOrder.Payload.Response.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentConverter {
    @Autowired
    private OrderConverter orderConverter;
    public PaymentResponse toPaymentResponse(PaymentEntity paymentEntity) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(paymentEntity.getId());
        paymentResponse.setPaymentStatus(paymentEntity.getPaymentStatus());
        paymentResponse.setIsPayed(paymentEntity.getPayed());
//        paymentResponse.setOrderId(paymentEntity.getOrderId());
//        paymentResponse.setUserId(paymentEntity.getUserId());
        return paymentResponse;
    }

    public PaymentEntity toPaymentEntity(PaymentResponse paymentResponse) {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(paymentResponse.getId());
        entity.setPayed(paymentResponse.getIsPayed());
        entity.setPaymentStatus(paymentResponse.getPaymentStatus());
//        entity.setUserId(paymentResponse.getUserId());
//        entity.setOrderId(paymentResponse.getOrderId());

        return entity;
    }
    public List<PaymentResponse> toPaymentResponseList(List<PaymentEntity> paymentEntities) {
        return paymentEntities.stream()
                .map(this::toPaymentResponse)
                .collect(Collectors.toList());
    }
}