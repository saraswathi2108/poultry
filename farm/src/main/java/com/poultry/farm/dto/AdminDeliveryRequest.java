package com.poultry.farm.dto;

import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Data
@Component
public class AdminDeliveryRequest {
    private String vehicleNumber;
    private String driverName;
    private String driverPhone;
    private LocalDateTime actualDeliveryTime;

    private Double weight;
    private int quantity;
    private Double pricePerKg;
    private Double totalAmount;

}