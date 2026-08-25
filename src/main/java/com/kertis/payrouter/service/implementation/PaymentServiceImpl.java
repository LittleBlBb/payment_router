package com.kertis.payrouter.service.implementation;

import com.kertis.payrouter.dto.CreatePaymentRequest;
import com.kertis.payrouter.dto.PaymentResponse;
import com.kertis.payrouter.model.Currency;
import com.kertis.payrouter.model.Order;
import com.kertis.payrouter.model.Payment;
import com.kertis.payrouter.model.PaymentStatus;
import com.kertis.payrouter.repository.OrderRepository;
import com.kertis.payrouter.repository.PaymentRepository;
import com.kertis.payrouter.service.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(() ->
                new IllegalArgumentException("order not found"));

        BigDecimal amount = request.getAmount();

        if (amount.doubleValue() < 0) throw new IllegalArgumentException("amount should be positive");

        Currency currency = request.getCurrency();

        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStatus(PaymentStatus.CREATED);
        Instant now = Instant.now();
        payment.setCreatedAt(now);
        payment.setUpdatedAt(now);

        return new PaymentResponse(paymentRepository.save(payment));
    }
}
