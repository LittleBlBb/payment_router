package com.kertis.payrouter.service.interfaces;

import com.kertis.payrouter.dto.CreatePaymentRequest;
import com.kertis.payrouter.dto.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request);

    PaymentResponse processPayment(UUID paymentId);
}
