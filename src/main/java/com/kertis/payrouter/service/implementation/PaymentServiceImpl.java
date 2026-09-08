package com.kertis.payrouter.service.implementation;

import com.kertis.payrouter.dto.CreatePaymentRequest;
import com.kertis.payrouter.dto.PaymentGatewayResult;
import com.kertis.payrouter.dto.PaymentResponse;
import com.kertis.payrouter.exception.AlreadyInProcessingOrCompletedException;
import com.kertis.payrouter.exception.NotFoundException;
import com.kertis.payrouter.exception.ValidationException;
import com.kertis.payrouter.model.Currency;
import com.kertis.payrouter.model.Order;
import com.kertis.payrouter.model.Payment;
import com.kertis.payrouter.model.PaymentStatus;
import com.kertis.payrouter.repository.OrderRepository;
import com.kertis.payrouter.repository.PaymentRepository;
import com.kertis.payrouter.service.interfaces.PaymentGateway;
import com.kertis.payrouter.service.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(() ->
                new NotFoundException("order not found"));

        BigDecimal amount = request.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 1)
            throw new ValidationException("amount should be positive");

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

    @Override
    public PaymentResponse processPayment(UUID uuid) {

        Payment payment = paymentRepository.findById(uuid).orElseThrow(() ->
                new NotFoundException("payment not found"));

        if (!payment.getStatus().equals(PaymentStatus.CREATED)){
            throw new AlreadyInProcessingOrCompletedException("payment already in process or completed");
        }

        payment.setStatus(PaymentStatus.PROCESSING);
        payment.setUpdatedAt(Instant.now());

        paymentRepository.save(payment);

        PaymentGatewayResult result = paymentGateway.processPayment(payment);

        payment.setStatus(result.getStatus());
        payment.setUpdatedAt(Instant.now());

        return new PaymentResponse(paymentRepository.save(payment));
    }
}
