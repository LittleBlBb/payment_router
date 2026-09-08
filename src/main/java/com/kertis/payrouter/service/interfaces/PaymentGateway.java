package com.kertis.payrouter.service.interfaces;

import com.kertis.payrouter.dto.PaymentGatewayResult;
import com.kertis.payrouter.model.Payment;

public interface PaymentGateway {

    PaymentGatewayResult processPayment(Payment payment);

}
