package com.kodlamaio.commonpackage.events.rental;

import com.kodlamaio.commonpackage.events.Event;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@RequiredArgsConstructor
public class RentalInvoiceCreatedEvent implements Event {
    private UUID carId;
    private LocalDateTime rentedAt;
    private String modelName;
    private String brandName;
    private double dailyPrice;
    private String plate;
    private String cardHolder;
    private String modelYear;
    private int rentedForDays;

}
