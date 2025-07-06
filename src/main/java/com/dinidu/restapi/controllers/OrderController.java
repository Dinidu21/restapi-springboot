package com.dinidu.restapi.controllers;

import com.dinidu.restapi.dtos.ApiResponse;
import com.dinidu.restapi.dtos.OrderDTO;
import com.dinidu.restapi.models.Order;
import com.dinidu.restapi.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /orders - Fetching all orders");
        Page<OrderDTO> page = orderService.getAllOrders(pageable);

        Map<String, Object> metadata = paginationMetadata(page);

        return ResponseEntity.ok(
                ApiResponse.<List<OrderDTO>>builder()
                        .success(true)
                        .message("Orders fetched successfully")
                        .data(page.getContent())
                        .metadata(metadata)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(@PathVariable Long id) {
        log.info("GET /orders/{} - Fetching order by id", id);
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(order, "Order fetched successfully"));
    }

    @GetMapping("/order-number/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderByOrderNumber(@PathVariable String orderNumber) {
        log.info("GET /orders/order-number/{} - Fetching order by order number", orderNumber);
        OrderDTO order = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success(order, "Order fetched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        log.info("POST /orders - Creating new order");
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdOrder, "Order created successfully"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Order.OrderStatus status) {
        log.info("PUT /orders/{}/status - Updating order status to {}", id, status);
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updatedOrder, "Order status updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable Long id) {
        log.info("DELETE /orders/{} - Cancelling order", id);
        orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Order cancelled successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByUserId(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /orders/user/{} - Fetching orders for user", userId);
        Page<OrderDTO> page = orderService.getOrdersByUserId(userId, pageable);

        Map<String, Object> metadata = paginationMetadata(page);

        return ResponseEntity.ok(
                ApiResponse.<List<OrderDTO>>builder()
                        .success(true)
                        .message("Orders for user fetched successfully")
                        .data(page.getContent())
                        .metadata(metadata)
                        .build()
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByStatus(
            @PathVariable Order.OrderStatus status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /orders/status/{} - Fetching orders by status", status);
        Page<OrderDTO> page = orderService.getOrdersByStatus(status, pageable);

        Map<String, Object> metadata = paginationMetadata(page);

        return ResponseEntity.ok(
                ApiResponse.<List<OrderDTO>>builder()
                        .success(true)
                        .message("Orders by status fetched successfully")
                        .data(page.getContent())
                        .metadata(metadata)
                        .build()
        );
    }

    private Map<String, Object> paginationMetadata(Page<?> page) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("currentPage", page.getNumber());
        metadata.put("totalPages", page.getTotalPages());
        metadata.put("totalItems", page.getTotalElements());
        metadata.put("pageSize", page.getSize());
        return metadata;
    }
}
