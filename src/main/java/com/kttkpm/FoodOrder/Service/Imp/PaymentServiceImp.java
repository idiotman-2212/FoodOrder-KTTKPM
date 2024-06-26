package com.kttkpm.FoodOrder.Service.Imp;

import com.kttkpm.FoodOrder.Entity.PaymentStatus;
import com.kttkpm.FoodOrder.Payload.Request.PaymentRequest;
import com.kttkpm.FoodOrder.Payload.Response.PaymentResponse;

import java.util.List;

public interface PaymentServiceImp {
    PaymentResponse createPayment(PaymentRequest paymentRequest);
    List<PaymentResponse> getAllPayments();
    PaymentResponse getPaymentById(int id);
    boolean updatePaymentById(int id, int idOrder,int idUser, boolean idPayed, PaymentStatus paymentStatus);
    boolean deletePaymentById(int id);

    /* List<PaymentEntity> getPaymentsByOrderId(int orderId);*/
}
