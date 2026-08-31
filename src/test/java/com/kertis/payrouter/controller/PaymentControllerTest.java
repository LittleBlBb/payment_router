package com.kertis.payrouter.controller;

import com.kertis.payrouter.dto.CreatePaymentRequest;
import com.kertis.payrouter.dto.PaymentResponse;
import com.kertis.payrouter.exception.OrderNotFoundException;
import com.kertis.payrouter.exception.ValidationException;
import com.kertis.payrouter.model.Currency;
import com.kertis.payrouter.model.PaymentStatus;
import com.kertis.payrouter.service.interfaces.PaymentService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void shouldCreatePayment() throws Exception {

        UUID paymentId = UUID.randomUUID();

        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(paymentId);
        response.setAmount(new BigDecimal("10.50"));
        response.setCurrency(Currency.RUB);
        response.setStatus(PaymentStatus.CREATED);

        when(paymentService.createPayment(any(CreatePaymentRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "orderId": 1,
                            "amount": 10.50,
                            "currency": "RUB"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(paymentId.toString()))
                .andExpect(jsonPath("$.amount").value(10.50))
                .andExpect(jsonPath("$.currency").value("RUB"))
                .andExpect(jsonPath("$.status").value("CREATED"));

        verify(paymentService).createPayment(any(CreatePaymentRequest.class));
    }

    @Test
    void shouldReturn400WhenOrderIdIsMissing() throws Exception{

        mockMvc.perform(post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                        """
                        {
                            "amount": 10.50,
                            "currency": "RUB"
                        }
                        """
                ))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(paymentService);
    }

    @Test
    void shouldReturn400WhenAmountIncorrect() throws Exception {

        when(paymentService.createPayment(any(CreatePaymentRequest.class)))
                .thenThrow(new ValidationException("amount should be positive"));

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                """
                                {
                                    "orderId": 1,
                                    "amount": 0,
                                    "currency": "RUB"
                                }
                                """
                        ))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenOrderNotFound() throws Exception{

        when(paymentService.createPayment(any(CreatePaymentRequest.class)))
                .thenThrow(new OrderNotFoundException("order not found"));

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(
                                """
                                {
                                    "orderId": 999,
                                    "amount": 10.50,
                                    "currency": "RUB"
                                }
                                """
                        ))
                .andExpect(status().isNotFound());

    }

}
