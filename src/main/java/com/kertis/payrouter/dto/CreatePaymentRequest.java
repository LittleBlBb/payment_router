package com.kertis.payrouter.dto;

import com.kertis.payrouter.model.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class CreatePaymentRequest {

    private Long orderId;

    private BigDecimal amount;

    private Currency currency;
}
