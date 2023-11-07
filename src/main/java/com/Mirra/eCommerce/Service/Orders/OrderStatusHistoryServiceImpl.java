package com.Mirra.eCommerce.Service.Orders;

import com.Mirra.eCommerce.Models.Orders.OrderStatusHistory;
import com.Mirra.eCommerce.Repository.Orders.OrderStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStatusHistoryServiceImpl implements OrderStatusHistoryService{


    @Autowired
    private OrderStatusHistoryRepository orderStatusHistoryRepository;
    @Override
    public List<OrderStatusHistory> getOrderStatusHistoryByOrderId(int orderId) {
        return orderStatusHistoryRepository.findByOrderId(orderId);
    }
}
