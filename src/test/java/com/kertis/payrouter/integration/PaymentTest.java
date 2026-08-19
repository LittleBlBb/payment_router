package com.kertis.payrouter.integration;

import com.kertis.payrouter.model.Currency;
import com.kertis.payrouter.model.Order;
import com.kertis.payrouter.model.Payment;
import com.kertis.payrouter.model.PaymentStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PaymentTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldSaveAndReadPayment() {

        Order order = new Order();
        order.setCreatedAt(Instant.now());

        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("10.5"));
        payment.setStatus(PaymentStatus.CREATED);
        payment.setCurrency(Currency.RUB);
        payment.setCreatedAt(Instant.now());
        payment.setUpdatedAt(Instant.now());

        entityManager.persist(order);
        entityManager.flush();

        Long id = order.getId();
        entityManager.clear();

        payment.setOrderId(id);
        entityManager.persist(payment);
        entityManager.flush();

        UUID paymentId = payment.getPaymentId();
        Payment result = entityManager.find(Payment.class, paymentId);

        assertNotNull(result);
        assertEquals(paymentId, result.getPaymentId());
        assertEquals(new BigDecimal("10.5"), result.getAmount());
        assertEquals(PaymentStatus.CREATED, result.getStatus());

    }

}
