package com.poultry.farm.repository;

import com.poultry.farm.entity.Order;
import com.poultry.farm.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByPhoneNumber(String phoneNumber);


    List<Order> findByVendorNameContainingIgnoreCase(String vendorName);


    List<Order> findByBatchCode(String batchCode);


    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByStatusAndDeliveryDate(OrderStatus status, LocalDate deliveryDate);

    Optional<Order> findByOrderCode(String orderCode);
}