package com.poultry.farm.dto;

import com.poultry.farm.entity.OrderStatus;
import lombok.Data;

@Data
public class AdminStatusUpdateRequest {
    private OrderStatus status;
    private String rejectionReason;


}