package com.kttkpm.FoodOrder.Service;

import com.kttkpm.FoodOrder.Entity.PaymentEntity;
import com.kttkpm.FoodOrder.Entity.PaymentStatus;
import com.kttkpm.FoodOrder.Helper.OrderConverter;
import com.kttkpm.FoodOrder.Helper.PaymentConverter;
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
    public boolean createPayment(int idOrder,int idUser, boolean isPayed, PaymentStatus paymentStatus) {
        PaymentEntity paymentEntity = new PaymentEntity();
//        paymentEntity.setOrderId(idOrder);
//        paymentEntity.setUserId(idUser);
        paymentEntity.setPayed(isPayed);
        paymentEntity.setPaymentStatus(paymentStatus);
        paymentEntity.setPaymentDate(new Date());

        paymentRepository.save(paymentEntity);

        // Gửi sự kiện Payment Successful đến Kafka
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setOrderId(idOrder);
        paymentResponse.setPayed(isPayed);
        paymentResponse.setPaymentStatus(paymentStatus);
        paymentResponse.setUserId(idUser);

        return true;
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
            existingPayment.setPayed(idPayed);
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
    /*@Override
    public List<PaymentEntity> getPaymentsByOrderId(int orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);

        return null;
    }*/
}