package com.kertis.payrouter.service;

import com.kertis.payrouter.dto.PaymentGatewayResult;
import com.kertis.payrouter.model.Payment;
import com.kertis.payrouter.model.PaymentStatus;

import java.math.BigDecimal;

public class MockPaymentGateway implements ProcessAble {

    @Override
    public PaymentGatewayResult processPayment(Payment payment) {
        if (payment.getAmount().compareTo(BigDecimal.valueOf(500000)) > 0) payment.setStatus(PaymentStatus.FAILED);
        else payment.setStatus(PaymentStatus.SUCCESS);
        return new PaymentGatewayResult(payment);
    }
}
