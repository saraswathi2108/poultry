package com.poultry.farm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String orderCode;


    private String vendorName;
    private String phoneNumber;
    private String shopName;
    private LocalDate deliveryDate;


    private int quantity;
    private int weight;
    private LocalDateTime orderDate;

    // Status tracking
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;


    private String vehicleNumber;
    private String driverName;
    private String driverPhone;
    private String address;
    private LocalDateTime actualDeliveryTime;

    private Long batchId;
    private String batchCode;
    private String breed;

    private Double receivedWeight;
    private int receivedQuantity;
    private Double pricePerKg;
    private Double totalPrice;


}