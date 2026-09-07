package com.kertis.payrouter.service;

import com.kertis.payrouter.dto.PaymentGatewayResult;
import com.kertis.payrouter.model.Payment;

public interface ProcessAble {
    PaymentGatewayResult processPayment(Payment payment);
}
