package com.kodlamaio.maintenanceservice.business.rules;


import com.kodlamaio.commonpackage.utils.exceptions.BusinessException;
import com.kodlamaio.maintenanceservice.api.clients.CarClient;
import com.kodlamaio.maintenanceservice.repository.MaintenanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MaintenanceBusinessRules {
    private final MaintenanceRepository repository;
    @Qualifier("com.kodlamaio.maintenanceservice.api.clients.CarClient")
    private final CarClient client;

    public void checkIfCarIsUnderMaintenance(UUID id) {
        if (repository.existsByCarIdAndIsCompletedIsFalse(id)) {
            throw new BusinessException("Messages.Maintenance.CarExists");
        }
    }

    public void checkIfCarIsNotUnderMaintenance(UUID carId) {
        if (!repository.existsByCarIdAndIsCompletedIsFalse(carId)) {
            throw new BusinessException("Messages.Maintenance.CarNotExists");
        }
    }

    public void checkIfMaintenanceExists(UUID maintenanceId) {
        if (!repository.existsById(maintenanceId)) {
            throw new BusinessException("Messages.Maintenance.NotExists");
        }
    }

    //    public void checkCarAvailabilityForMaintenance(State state) {
//        if(state.equals(State.RENTED)){
//            throw new BusinessException(Messages.Maintenance.CarIsRented);
//        }
//    }
    public void ensureCarIsAvailable(UUID carId) throws InterruptedException {
        var response = client.checkIfCarAvailable(carId);
        if (!response.isSuccess()) {
            throw new BusinessException(response.getMessage());
        }
    }

}