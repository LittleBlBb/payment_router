package com.kertis.payrouter.service;

import com.kertis.payrouter.dto.PaymentGatewayResult;
import com.kertis.payrouter.model.Payment;

public class PaymentGateway {
    
    private final ProcessAble gateway;
    
    public PaymentGateway(ProcessAble gateway){
        this.gateway = gateway;
    }

    public PaymentGatewayResult processPayment(Payment payment) {
        return gateway.processPayment(payment);
    }

}
