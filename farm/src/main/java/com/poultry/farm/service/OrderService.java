package com.poultry.farm.service;

import com.poultry.farm.dto.AdminDeliveryRequest;
import com.poultry.farm.dto.AdminStatusUpdateRequest;
import com.poultry.farm.dto.VendorOrderRequest;
import com.poultry.farm.entity.Batch;
import com.poultry.farm.entity.Order;
import com.poultry.farm.entity.OrderStatus;
import com.poultry.farm.exceptions.InsufficientStockException;
import com.poultry.farm.exceptions.InvalidOrderStateException;
import com.poultry.farm.exceptions.ResourceNotFoundException;
import com.poultry.farm.repository.BatchRepository;
import com.poultry.farm.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private AdminDeliveryRequest adminDeliveryRequest;

    // Vendor places order
    public Order placeVendorOrder(VendorOrderRequest request) {

        // Validate batch
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + request.getBatchId()));

        // Check availability
        if (batch.getAvailableHens() < request.getQuantity()) {
            throw new InsufficientStockException(
                    "Requested quantity: " + request.getQuantity() +
                            ", Available: " + batch.getAvailableHens()
            );
        }

        // Update batch availability
        batch.setWeight(batch.getWeight() - request.getWeight());
        batch.setAvailableHens(batch.getAvailableHens() - request.getQuantity());
        batchRepository.save(batch);

        // Create and save order
        Order order = new Order();
        order.setOrderCode(generateOrderCode());
        order.setBatchId(batch.getId());
        order.setBatchCode(batch.getBatchCode());
        order.setBreed(batch.getBreed()); // Store breed info
        order.setQuantity(request.getQuantity());
        order.setWeight(request.getWeight());
        order.setVendorName(request.getVendorName());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setShopName(request.getShopName());
        order.setAddress(request.getAddress());
        order.setDeliveryDate(request.getDeliveryDate());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    // Get orders by status (for admin)
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    // Get specific order by ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    // Get orders by phone number (for vendor)
    public List<Order> getOrdersByPhoneNumber(String phoneNumber) {
        return orderRepository.findByPhoneNumber(phoneNumber);
    }

    // Get all orders (for admin dashboard)
    public List<Order> getAllOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    // Update order status (APPROVED/REJECTED)
    public Order updateOrderStatus(Long orderId, AdminStatusUpdateRequest request) {
        Order order = getOrderById(orderId);

        // If rejecting, return stock to batch
        if (request.getStatus() == OrderStatus.REJECTED && order.getStatus() == OrderStatus.PENDING) {
            returnStockToBatch(order);
        }

        // If approving a previously rejected order, deduct stock again
        if (request.getStatus() == OrderStatus.APPROVED && order.getStatus() == OrderStatus.REJECTED) {
            deductStockFromBatch(order);
        }

        order.setStatus(request.getStatus());
        return orderRepository.save(order);
    }

    // Add delivery details (when dispatching)
    public Order addDeliveryDetails(Long orderId, AdminDeliveryRequest request) {
        Order order = getOrderById(orderId);

        // Validate order status
//        if (order.getStatus() != OrderStatus.APPROVED) {
//            throw new InvalidOrderStateException("Order must be APPROVED before adding delivery details");
//        }

        // Set delivery details
        order.setVehicleNumber(request.getVehicleNumber());
        order.setDriverName(request.getDriverName());
        order.setDriverPhone(request.getDriverPhone());
        order.setActualDeliveryTime(request.getActualDeliveryTime());
        order.setStatus(OrderStatus.DISPATCHED);
        order.setReceivedWeight(request.getWeight());
        order.setReceivedQuantity(request.getQuantity());
        order.setPricePerKg(request.getPricePerKg());
        order.setTotalPrice(request.getTotalAmount());

        return orderRepository.save(order);
    }

    // Mark order as delivered
    public Order markAsDelivered(Long orderId) {
        Order order = getOrderById(orderId);

        if (order.getStatus() != OrderStatus.DISPATCHED) {
            throw new InvalidOrderStateException("Order must be DISPATCHED before marking as delivered");
        }

        order.setStatus(OrderStatus.DELIVERED);
        if (order.getActualDeliveryTime() == null) {
            order.setActualDeliveryTime(LocalDateTime.now());
        }

        return orderRepository.save(order);
    }

    // Helper method to return stock when order is rejected
    private void returnStockToBatch(Order order) {
        Batch batch = batchRepository.findById(order.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));

        batch.setAvailableHens(batch.getAvailableHens() + order.getQuantity());
        batchRepository.save(batch);
    }

    // Helper method to deduct stock when order is approved
    private void deductStockFromBatch(Order order) {
        Batch batch = batchRepository.findById(order.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));

        if (batch.getAvailableHens() < order.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock to approve this order");
        }

        batch.setAvailableHens(batch.getAvailableHens() - order.getQuantity());
        batchRepository.save(batch);
    }

    // Generate unique order code
    private String generateOrderCode() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "ORD-" + timestamp + "-" + random;
    }
}