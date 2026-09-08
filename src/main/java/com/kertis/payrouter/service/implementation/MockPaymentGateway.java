package com.kertis.payrouter.service.implementation;

import com.kertis.payrouter.dto.PaymentGatewayResult;
import com.kertis.payrouter.model.Payment;
import com.kertis.payrouter.model.PaymentStatus;
import com.kertis.payrouter.service.interfaces.PaymentGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MockPaymentGateway implements PaymentGateway {

    @Value("{$MOCK_GATEWAY_MAX_AMOUNT}")
    private BigDecimal MOCK_GATEWAY_MAX_AMOUNT;

    @Override
    public PaymentGatewayResult processPayment(Payment payment) {
        if (payment.getAmount().compareTo(MOCK_GATEWAY_MAX_AMOUNT) > 1) return new PaymentGatewayResult(PaymentStatus.FAILED);
        else return new PaymentGatewayResult(PaymentStatus.SUCCESS);
    }
}
