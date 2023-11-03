package com.Mirra.eCommerce.Service.Orders;

import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Returns.ReturnStatus;

import java.util.List;

public interface OrderItemService {
     void updateOrderItem(OrderItem orderItem);

    public OrderItem getOrderItemById(int orderItemId);

    public List<OrderItem> findByOrderIdAndReturnStatus(int orderId, ReturnStatus returnStatus);
}
