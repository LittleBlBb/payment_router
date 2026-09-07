package com.kertis.payrouter.dto;

import com.kertis.payrouter.model.Currency;
import com.kertis.payrouter.model.Payment;
import com.kertis.payrouter.model.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class PaymentGatewayResult {
    private UUID paymentId;
    private Long orderId;
    private BigDecimal amount;
    private Currency currency;
    private PaymentStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public PaymentGatewayResult(Payment payment) {
        paymentId = payment.getPaymentId();
        orderId = payment.getOrderId();
        amount = payment.getAmount();
        currency = payment.getCurrency();
        status = payment.getStatus();
        createdAt = payment.getCreatedAt();
        updatedAt = payment.getUpdatedAt();

    }
}
