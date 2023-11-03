package com.Mirra.eCommerce.Service.Orders;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderItem;

import java.util.List;

public interface OrderService {

    public void saveOrder(Order order);
    public List<Order> getAllOrders();
    public Order getOrderById(int orderId);
    public List<Order> findOrdersByUserId(int userId);

    public boolean cancelOrder(int orderId);
    public OrderItem getOrderItemById(int orderItemId);
}
