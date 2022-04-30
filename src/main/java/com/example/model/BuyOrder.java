package com.example.model;

public class BuyOrder extends OrderDetails {

    public BuyOrder(String orderId, Long timeInMilliSec, String stock, Double price, Integer quantity) {
        super(orderId, timeInMilliSec, stock, OrderType.buy, price, quantity);
    }
}
