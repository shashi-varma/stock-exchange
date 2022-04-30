package com.example;

import com.example.model.BuyOrder;
import com.example.model.SellOrder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class OrderBook {

    private final Map<String, PriorityQueue<BuyOrder>> buyOrderBook;
    private final Map<String, PriorityQueue<SellOrder>> sellOrderBook;

    private final Comparator<BuyOrder> buyOrderComparator;
    private final Comparator<SellOrder> sellOrderComparator;

    public OrderBook(){
        buyOrderBook = new HashMap<>();
        sellOrderBook = new HashMap<>();
        buyOrderComparator = new BuyOrderComparator();
        sellOrderComparator = new SellOrderComparator();
    }

    public PriorityQueue<BuyOrder> getBuyOrderMarketDepth(String stock){
        if(!buyOrderBook.containsKey(stock)) buyOrderBook.put(stock, new PriorityQueue<>(buyOrderComparator));
        return buyOrderBook.get(stock);
    }

    public PriorityQueue<SellOrder> getSellOrderMarketDepth(String stock){
        if(!sellOrderBook.containsKey(stock)) sellOrderBook.put(stock, new PriorityQueue<SellOrder>(sellOrderComparator));
        return sellOrderBook.get(stock);
    }


    private static class BuyOrderComparator implements Comparator<BuyOrder>{
        @Override
        public int compare(BuyOrder o1, BuyOrder o2) {
            // buy orders having greater sell price are given priority and sits at the top of market depth
            if( o1.getPrice() > o2.getPrice() ){
                return -1;
            } else if( o1.getPrice() < o2.getPrice() ){
                return 1;
            } else {
                // if price is same, priority needs to be given to order which came first
                if( o1.getTimeInMilliSec() > o2.getTimeInMilliSec() ) return 1;
                else return -1;
            }
        }
    }

    private static class SellOrderComparator implements Comparator<SellOrder>{
        @Override
        public int compare(SellOrder o1, SellOrder o2) {
            // sell orders having lesser sell price are given priority and sits at the top of market depth
            if( o1.getPrice() < o2.getPrice() ){
                return -1;
            } else if( o1.getPrice() > o2.getPrice() ){
                return 1;
            } else {
                // if price is same, priority needs to be given to order which came first
                if( o1.getTimeInMilliSec() > o2.getTimeInMilliSec() ) return 1;
                else return -1;
            }
        }
    }
}
