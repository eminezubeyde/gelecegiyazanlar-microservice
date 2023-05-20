package com.kodlamaio.maintenanceservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String information;
    private Boolean isCompleted;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private UUID carId;
}