package com.kertis.payrouter.service.interfaces;

import com.kertis.payrouter.dto.CreatePaymentRequest;
import com.kertis.payrouter.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request);
}
