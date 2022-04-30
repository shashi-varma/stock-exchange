package com.example;

import com.example.model.BuyOrder;
import com.example.model.OrderDetails;
import com.example.model.OrderType;
import com.example.model.SellOrder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility class to open the input file and read and parse the order details
 */
public class OrderReader {

    BufferedReader br;

    public OrderReader(String fileName) throws FileNotFoundException {
        FileReader fr = new FileReader(fileName);
        br = new BufferedReader(fr);
    }

    /**
     * Method to get the next order from the input file.
     * Returns null if end of the file is reached
     */
    public OrderDetails next() throws IOException {
        String line = br.readLine();
        if(line == null) return null;
        String[] order = line.split(" ");
        assert order.length == 6 : "invalid order found in the file";

        String orderId = order[0];
        Long timeInMilliSec = processTimeInMilliSeconds(order[1]);
        String stock = order[2];
        OrderType orderType = OrderType.valueOf(order[3]);
        Double price = Double.parseDouble(order[4]);
        Integer quantity = Integer.parseInt(order[5]);

        if(OrderType.buy.equals(orderType)) return new BuyOrder(orderId, timeInMilliSec, stock, price, quantity);
        else return new SellOrder(orderId, timeInMilliSec, stock, price, quantity);
    }

    /**
     * Processing time in milliseconds to keep the code robust to changes in time format in order
     * Even if date or seconds are added in the time format, only this part requires change,
     *      rest of the application doesn't need to go through any change
     *      since time is already used in Long and considered in milliseconds
     * @param timeInHoursAndMinutes
     * @return
     */
    private Long processTimeInMilliSeconds(String timeInHoursAndMinutes){
        String[] time = timeInHoursAndMinutes.split(":");
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        return (hours*60L + minutes)*60*1000;
    }

    public void close() throws IOException {
        br.close();
    }
}
