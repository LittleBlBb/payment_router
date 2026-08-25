package com.kertis.payrouter.controller;

import com.kertis.payrouter.dto.CreatePaymentRequest;
import com.kertis.payrouter.dto.PaymentResponse;
import com.kertis.payrouter.service.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("create_payment")
    public PaymentResponse createPayment(@RequestBody CreatePaymentRequest request){

        return paymentService.createPayment(request);
    }
}
