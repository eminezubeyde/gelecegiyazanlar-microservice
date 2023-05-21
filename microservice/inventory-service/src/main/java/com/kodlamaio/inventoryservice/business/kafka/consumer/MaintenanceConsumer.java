package com.kodlamaio.inventoryservice.business.kafka.consumer;

import com.kodlamaio.commonpackage.events.maintenance.MaintenanceCompletedEvent;
import com.kodlamaio.commonpackage.events.maintenance.MaintenanceCreatedEvent;
import com.kodlamaio.commonpackage.events.maintenance.MaintenanceDeletedEvent;
import com.kodlamaio.inventoryservice.business.abstracts.CarService;
import com.kodlamaio.inventoryservice.entities.enums.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MaintenanceConsumer {
    private final CarService service;

    @KafkaListener(
            topics = "maintenance-created",
            groupId = "inventory-maintenance-create"
    )
    public void consume(MaintenanceCreatedEvent event) {
        //change car state
        service.changeStateByCarId(State.Maintenance, event.getCarId());
        log.info("maintenance created event consumed {}", event);
    }

    @KafkaListener(
            topics = "maintenance-deleted",
            groupId = "inventory-maintenance-delete"
    )
    public void consume(MaintenanceDeletedEvent event) {
        //change car state
        service.changeStateByCarId(State.Available, event.getCarId());
        log.info("maintenance deleted event consumed {}", event);
    }

    @KafkaListener(
            topics = "maintenance-completed",
            groupId = "inventory-maintenance-complete"
    )
    public void consume(MaintenanceCompletedEvent event) {
        //change car state
        service.changeStateByCarId(State.Available, event.getCarId());
        log.info("maintenance event completed {}", event);
    }
}