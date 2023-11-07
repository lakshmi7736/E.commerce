package com.Mirra.eCommerce.Service.Orders;

import com.Mirra.eCommerce.Models.Orders.OrderStatusHistory;

import java.util.List;

public interface OrderStatusHistoryService {

    public List<OrderStatusHistory> getOrderStatusHistoryByOrderId(int orderId);
}
