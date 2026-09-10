package com.kertis.payrouter.integration;

import com.kertis.payrouter.dto.PaymentGatewayResult;
import com.kertis.payrouter.dto.PaymentResponse;
import com.kertis.payrouter.exception.AlreadyInProcessingOrCompletedException;
import com.kertis.payrouter.exception.NotFoundException;
import com.kertis.payrouter.model.Currency;
import com.kertis.payrouter.model.Payment;
import com.kertis.payrouter.model.PaymentStatus;
import com.kertis.payrouter.repository.PaymentRepository;
import com.kertis.payrouter.service.implementation.PaymentServiceImpl;
import com.kertis.payrouter.service.interfaces.PaymentGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void shouldProcessPaymentSuccessfully() {

        Long orderId = 1L;
        UUID uuid = UUID.randomUUID();

        Payment payment = new Payment();
        payment.setPaymentId(uuid);
        payment.setOrderId(orderId);
        payment.setAmount(new BigDecimal("100.00"));
        payment.setCurrency(Currency.RUB);
        payment.setStatus(PaymentStatus.CREATED);

        when(paymentRepository.findById(uuid))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(paymentGateway.processPayment(payment))
                .thenReturn(new PaymentGatewayResult(PaymentStatus.SUCCESS));

        PaymentResponse result = paymentService.processPayment(uuid);

        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        assertEquals(uuid, result.getPaymentId());

        verify(paymentRepository).findById(uuid);
        verify(paymentGateway).processPayment(payment);
        verify(paymentRepository, times(2)).save(payment);
    }

    @Test
    void shouldProcessPaymentFailed() {

        Long orderId = 1L;
        UUID uuid = UUID.randomUUID();

        Payment payment = new Payment();
        payment.setPaymentId(uuid);
        payment.setOrderId(orderId);
        payment.setAmount(new BigDecimal("100000000.00"));
        payment.setCurrency(Currency.RUB);
        payment.setStatus(PaymentStatus.CREATED);

        when(paymentRepository.findById(uuid))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(paymentGateway.processPayment(payment))
                .thenReturn(new PaymentGatewayResult(PaymentStatus.FAILED));

        PaymentResponse result = paymentService.processPayment(uuid);

        assertEquals(PaymentStatus.FAILED, result.getStatus());
        assertEquals(uuid, result.getPaymentId());

        verify(paymentRepository).findById(uuid);
        verify(paymentGateway).processPayment(payment);
        verify(paymentRepository, times(2)).save(payment);
    }

    @Test
    void shouldThrowExceptionWhenPaymentAlreadyProcessed() {

        UUID uuid = UUID.randomUUID();

        Payment payment = new Payment();
        payment.setPaymentId(uuid);
        payment.setOrderId(1L);
        payment.setAmount(new BigDecimal("100.00"));
        payment.setCurrency(Currency.RUB);
        payment.setStatus(PaymentStatus.SUCCESS);

        when(paymentRepository.findById(uuid))
                .thenReturn(Optional.of(payment));

        assertThrows(
                AlreadyInProcessingOrCompletedException.class,
                () -> paymentService.processPayment(uuid)
        );

        verify(paymentRepository).findById(uuid);
        verify(paymentGateway, never()).processPayment(any(Payment.class));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void shouldThrowExceptionWhenPaymentNotFound() {

        UUID uuid = UUID.randomUUID();

        when(paymentRepository.findById(uuid))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> paymentService.processPayment(uuid)
        );

        verify(paymentRepository).findById(uuid);
        verify(paymentGateway, never()).processPayment(any(Payment.class));
        verify(paymentRepository, never()).save(any(Payment.class));
    }
}
