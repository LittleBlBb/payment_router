package com.kertis.payrouter.integration;

import com.kertis.payrouter.dto.PaymentGatewayResult;
import com.kertis.payrouter.model.Payment;
import com.kertis.payrouter.model.PaymentStatus;
import com.kertis.payrouter.service.implementation.MockPaymentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockPaymentGatewayTest {

    private MockPaymentGateway paymentGateway;

    @BeforeEach
    void setUp(){
        paymentGateway = new MockPaymentGateway();

        ReflectionTestUtils.setField(
                paymentGateway,
                "mockGatewayMaxAmount",
                new BigDecimal("1000.00")
        );
    }

    @Test
    void shouldReturnSuccessWhenAmountIsLessThanMaxAmount() {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("100.00"));

        PaymentGatewayResult result = paymentGateway.processPayment(payment);

        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
    }

    @Test
    void shouldReturnSuccessWhenAmountIsEqualsMaxAmount() {

        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("1000.00"));

        PaymentGatewayResult result = paymentGateway.processPayment(payment);

        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
    }

    @Test
    void shouldReturnFailedWhenAmountIsGreaterThanMaxAmount() {

        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("10000.00"));

        PaymentGatewayResult result = paymentGateway.processPayment(payment);

        assertEquals(PaymentStatus.FAILED, result.getStatus());
    }
}
