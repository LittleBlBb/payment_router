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
    private PaymentStatus status;

    public PaymentGatewayResult(PaymentStatus status) {
        this.status = status;
    }
}
