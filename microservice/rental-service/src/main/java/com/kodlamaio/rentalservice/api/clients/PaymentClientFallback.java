package com.kodlamaio.rentalservice.api.clients;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentClientFallback implements PaymentClient{
    @Override
    @Retry(name = "process-rental-payment")
    public ClientResponse processRentalPayment(CreateRentalPaymentRequest request) {
        throw new RuntimeException("PAYMENT SERVICE IS DOWN!");
    }
}
