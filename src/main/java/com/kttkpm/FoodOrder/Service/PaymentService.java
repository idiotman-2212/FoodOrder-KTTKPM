package com.kttkpm.FoodOrder.Service;

import com.kttkpm.FoodOrder.Entity.OrderEntity;
import com.kttkpm.FoodOrder.Entity.PaymentEntity;
import com.kttkpm.FoodOrder.Entity.PaymentStatus;
import com.kttkpm.FoodOrder.Helper.OrderConverter;
import com.kttkpm.FoodOrder.Helper.PaymentConverter;
import com.kttkpm.FoodOrder.Payload.Request.PaymentRequest;
import com.kttkpm.FoodOrder.Payload.Response.PaymentResponse;
import com.kttkpm.FoodOrder.Repository.OrderRepository;
import com.kttkpm.FoodOrder.Repository.PaymentRepository;
import com.kttkpm.FoodOrder.Service.Imp.PaymentServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PaymentService implements PaymentServiceImp {
    @Autowired
    private OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final PaymentRepository paymentRepository;
    private final PaymentConverter paymentConverter;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, PaymentConverter paymentConverter, OrderConverter orderConverter) {
        this.paymentRepository = paymentRepository;
        this.paymentConverter = paymentConverter;
        this.orderConverter = orderConverter;
    }
    // Trong phương thức thực hiện thanh toán
    @Override
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        OrderEntity order = orderRepository.findById(paymentRequest.getOrderId()).orElse(null);

        PaymentEntity payment = new PaymentEntity();
        payment.setIsPayed(paymentRequest.getIsPayed());
        payment.setPaymentStatus(PaymentStatus.valueOf(paymentRequest.getPaymentStatus()));
        payment.setPaymentDate(new Date());
        payment.setOrder(order);
        payment.setTransactionId(paymentRequest.getTransactionId());
        payment.setPaymentAmount(paymentRequest.getPaymentAmount());

        PaymentEntity savedPayment = paymentRepository.save(payment);
        return new PaymentResponse(savedPayment.getId(), savedPayment.getIsPayed(), savedPayment.getPaymentStatus().getStatus(),
                savedPayment.getPaymentDate(), savedPayment.getOrder().getId(), savedPayment.getTransactionId(), savedPayment.getPaymentAmount());
    }



    @Override
    public List<PaymentResponse> getAllPayments() {
        List<PaymentEntity> payments = paymentRepository.findAll();
        List<PaymentResponse> paymentResponses = new ArrayList<>();

        for (PaymentEntity paymentEntity : payments) {
            PaymentResponse paymentResponse = paymentConverter.toPaymentResponse(paymentEntity);
            paymentResponses.add(paymentResponse);
        }

        return paymentResponses;
    }

    @Override
    public PaymentResponse getPaymentById(int id) {
        Optional<PaymentEntity> payment = paymentRepository.findById(id);
        return payment.map(paymentConverter::toPaymentResponse).orElse(null);
    }

    @Override
    public boolean updatePaymentById(int id, int idOrder,int idUser, boolean idPayed, PaymentStatus paymentStatus) {
        Optional<PaymentEntity> optionalPayment = paymentRepository.findById(id);
        List<PaymentResponse> paymentResponses = new ArrayList<>();

        if (optionalPayment.isPresent()) {
            PaymentEntity existingPayment = optionalPayment.get();
            existingPayment.setIsPayed(idPayed);
            existingPayment.setPaymentDate(new Date());
            existingPayment.setPaymentStatus(paymentStatus);
//            existingPayment.setUserId(idUser);
//            existingPayment.setOrderId(idOrder);

            paymentRepository.save(existingPayment);
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePaymentById(int id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}