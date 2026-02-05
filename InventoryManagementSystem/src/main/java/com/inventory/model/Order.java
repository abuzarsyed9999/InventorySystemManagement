package com.inventory.model;

import java.time.LocalDateTime;

public class Order {
    private int orderId;
    private int productId;
    private int quantity;
    private String orderType; // "PURCHASE" or "SALE"
    private LocalDateTime orderDate;
    private String status; // e.g., "Completed"

    public Order() {}

    public Order(int productId, int quantity, String orderType) {
        this.productId = productId;
        this.quantity = quantity;
        this.orderType = orderType;
        this.status = "Completed";
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + orderId +
                ", product=" + productId +
                ", qty=" + quantity +
                ", type='" + orderType + '\'' +
                ", date=" + (orderDate != null ? orderDate.toString().substring(0, 19) : "N/A") +
                ", status='" + status + '\'' +
                '}';
    }
}