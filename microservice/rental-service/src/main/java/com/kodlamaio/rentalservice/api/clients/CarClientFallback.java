package com.kodlamaio.rentalservice.api.clients;

import com.kodlamaio.commonpackage.events.inventory.CarCreatedEvent;
import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@Component
public class CarClientFallback implements CarClient{

    @Override
    @Retry(name = "retry-rental-client")
    public ClientResponse checkIfCarAvailable(UUID carId) {
        throw new RuntimeException("INVENTORY SERVICE IS DOWN!");
    }

    @Override
    public CarCreatedEvent getById(UUID carId) {
        throw new RuntimeException("INVENTORY SERVICE IS DOWN!");
    }
}