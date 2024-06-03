package com.kttkpm.FoodOrder.Payload.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kttkpm.FoodOrder.Entity.PaymentStatus;

public class PaymentResponse {
    private int id;
    private Boolean isPayed;
    private PaymentStatus paymentStatus;

    private int orderId;
    @JsonProperty("order")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderResponse orderResponse;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderResponse getOrderResponse() {
        return orderResponse;
    }

    public void setOrderResponse(OrderResponse orderResponse) {
        this.orderResponse = orderResponse;
    }

    public Boolean getPayed() {
        return isPayed;
    }

    public void setPayed(Boolean payed) {
        isPayed = payed;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OrderResponse getOrderReponse() {
        return orderResponse;
    }

    public void setOrderReponse(OrderResponse orderResponse) {
        this.orderResponse = orderResponse;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

}