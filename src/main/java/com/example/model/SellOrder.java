package com.example.model;

public class SellOrder extends OrderDetails {

    public SellOrder(String orderId, Long timeInMilliSec, String stock, Double price, Integer quantity) {
        super(orderId, timeInMilliSec, stock, OrderType.sell, price, quantity);
    }
}
