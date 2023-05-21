package com.kodlamaio.commonpackage.events.payment;

import com.kodlamaio.commonpackage.events.Event;

import java.util.UUID;

public class PaymentCreatedEvent implements Event {
    private UUID paymentId;
}
