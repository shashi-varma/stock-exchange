package com.example;

import com.example.model.OrderDetails;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String filePath = args[0];
        OrderReader orderReader = new OrderReader(filePath);

        OrderBook orderBook = new OrderBook();
        OrderMatcher orderMatcher = OrderMatcher.getInstance();

        // read orders one by one and match it with existing orders of opposite type
        OrderDetails orderDetails = orderReader.next();
        while(orderDetails != null){
            orderMatcher.match(orderBook, orderDetails);
            orderDetails = orderReader.next();
        }

        orderReader.close();
    }
}
