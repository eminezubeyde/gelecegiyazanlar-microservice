package com.kodlamaio.invoiceservice.business.dto.responses.update;

import java.time.LocalDateTime;

public class UpdateInvoiceResponse {
    private String id;
    private String cardHolder;
    private String modelName;
    private String brandName;
    private String plate;
    private int modelYear;
    private double dailyPrice;
    private double totalPrice;
    private int rentedForDays;
    private LocalDateTime rentedAt;
}