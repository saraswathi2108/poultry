package com.poultry.farm.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VendorOrderRequest {

    private Long batchId;
    private String batchCode;
    private int quantity;
    private int weight;
    private String vendorName;
    private String phoneNumber;
    private String shopName;
    private String address;
    private LocalDate deliveryDate;


}