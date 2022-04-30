package com.example;

import com.example.model.BuyOrder;
import com.example.model.OrderDetails;
import com.example.model.OrderType;
import com.example.model.SellOrder;

import java.util.PriorityQueue;

/**
 * A stateless class consisting the order matching logic
 */
public class OrderMatcher {

    // Containing instance inside a static class for threadsafe lazy initialization
    private static class OrderMatcherContainer {
        public static final OrderMatcher INSTANCE = new OrderMatcher();
    }

    private OrderMatcher() {}

    public static OrderMatcher getInstance() { return OrderMatcherContainer.INSTANCE; }

    public void match(OrderBook orderBook, OrderDetails orderDetails){
        // get corresponding stock's buy and sell market depths from order book
        PriorityQueue<BuyOrder> buyOrderMarketDepth = orderBook.getBuyOrderMarketDepth(orderDetails.getStock());
        PriorityQueue<SellOrder> sellOrderMarketDepth = orderBook.getSellOrderMarketDepth(orderDetails.getStock());

        if(orderDetails.getOrderType().equals(OrderType.buy)){
            BuyOrder buyOrder = (BuyOrder) orderDetails;
            // if there are no sell orders present, add the buy order to the 'pending list / market depth' and exit
            if(sellOrderMarketDepth.isEmpty()) buyOrderMarketDepth.add(buyOrder);
            else matchBuyOrder( buyOrder, buyOrderMarketDepth, sellOrderMarketDepth );
        } else {
            SellOrder sellOrder = (SellOrder) orderDetails;
            // if there are no buy orders present, add the sell order to the 'pending list / market depth' and exit
            if(buyOrderMarketDepth.isEmpty()) sellOrderMarketDepth.add(sellOrder);
            else matchSellOrder( sellOrder, buyOrderMarketDepth, sellOrderMarketDepth );
        }
    }

    public void matchBuyOrder(BuyOrder buyOrder, PriorityQueue<BuyOrder> buyOrderMarketDepth, PriorityQueue<SellOrder> sellOrderMarketDepth){
        while(!sellOrderMarketDepth.isEmpty()){
            SellOrder topSellOrder = sellOrderMarketDepth.peek();
            if (topSellOrder.getPrice() < buyOrder.getPrice()){
                // these orders match
                printMatchedOrders(buyOrder, topSellOrder);

                if(topSellOrder.getQuantity() > buyOrder.getQuantity()){
                    // buy order got completely matched, deducting the quantity from sell order and exiting
                    topSellOrder.deductQuantity( buyOrder.getQuantity() );
                    break;
                } else if(topSellOrder.getQuantity() < buyOrder.getQuantity()){
                    // buy order did not get completely matched, deducting the sold quantity from buy order and rerunning the loop to check net sell order
                    sellOrderMarketDepth.poll();
                    buyOrder.deductQuantity( topSellOrder.getQuantity() );
                } else {
                    // buy order got exactly matched, removing the sell order and exiting
                    sellOrderMarketDepth.poll();
                    break;
                }
            } else {
                // the buy value is lesser than the least possible sell value available. Adding it to pending list / market depth
                buyOrderMarketDepth.add(buyOrder);
                break;
            }
        }
    }

    public void matchSellOrder(SellOrder sellOrder, PriorityQueue<BuyOrder> buyOrderMarketDepth, PriorityQueue<SellOrder> sellOrderMarketDepth){
        while(!buyOrderMarketDepth.isEmpty()){
            BuyOrder topBuyOrder = buyOrderMarketDepth.peek();
            if (topBuyOrder.getPrice() > sellOrder.getPrice()){
                // these orders match
                printMatchedOrders(topBuyOrder, sellOrder);

                if(topBuyOrder.getQuantity() > sellOrder.getQuantity()){
                    // buy order got completely matched, deducting the quantity from sell order and exiting
                    topBuyOrder.deductQuantity( sellOrder.getQuantity() );
                    break;
                } else if(topBuyOrder.getQuantity() < sellOrder.getQuantity()){
                    // buy order did not get completely matched, deducting the sold quantity from buy order and rerunning the loop to check net sell order
                    buyOrderMarketDepth.poll();
                    sellOrder.deductQuantity( topBuyOrder.getQuantity() );
                } else {
                    // buy order got exactly matched, removing the sell order and exiting
                    buyOrderMarketDepth.poll();
                    break;
                }
            } else {
                // the sell value is greater than the highest possible buy value available. Adding it to pending list / market depth
                sellOrderMarketDepth.add(sellOrder);
                break;
            }
        }
    }

    private void printMatchedOrders(BuyOrder buyOrder, SellOrder sellOrder) {
        System.out.printf("%s %.2f %d %s%n",
                buyOrder.getOrderId(),
                sellOrder.getPrice(),
                Math.min(buyOrder.getQuantity(), sellOrder.getQuantity()),
                sellOrder.getOrderId());
    }
}
