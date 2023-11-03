package com.Mirra.eCommerce.Service.Orders;

import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Returns.ReturnStatus;
import com.Mirra.eCommerce.Repository.Orders.OrderItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemServiceImpl implements OrderItemService{

    @Autowired
    private OrderItemsRepository orderItemRepository;



    @Override
    public void updateOrderItem(OrderItem orderItem) {
        // You can update the OrderItem entity and save it back to the database
        orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem getOrderItemById(int orderItemId) {
        // Use the JpaRepository method findById to retrieve an OrderItem by its ID
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(orderItemId);

        // Check if the OrderItem exists, and if so, return it; otherwise, return null or handle as needed
        return optionalOrderItem.orElse(null);
    }

    @Override
    public List<OrderItem> findByOrderIdAndReturnStatus(int orderId, ReturnStatus returnStatus) {
        return orderItemRepository.findByOrderIdAndReturnStatus(orderId, returnStatus);
    }
}
