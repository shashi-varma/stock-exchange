package com.example.model;

public abstract class OrderDetails {

    private String orderId;

    private Long timeInMilliSec;

    private String stock;

    private OrderType orderType;

    private Double price;

    private Integer quantity;

    protected OrderDetails(String orderId, Long timeInMilliSec, String stock, OrderType orderType, Double price, Integer quantity) {
        this.orderId = orderId;
        this.timeInMilliSec = timeInMilliSec;
        this.stock = stock;
        this.orderType = orderType;
        this.price = price;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public Long getTimeInMilliSec() {
        return timeInMilliSec;
    }

    public String getStock() {
        return stock;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void deductQuantity(Integer quantityToReduce) {
        this.quantity = this.quantity - quantityToReduce;
    }
}
