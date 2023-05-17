package com.kodlamaio.rentalservice.api.clients;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class CarClientFallback implements CarClient{

    @Override
    @Retry(name = "retry-rental-client")
    public ClientResponse checkIfCarAvailable(UUID carId) {
        throw new RuntimeException("INVENTORY SERVICE IS DOWN!");
    }
}