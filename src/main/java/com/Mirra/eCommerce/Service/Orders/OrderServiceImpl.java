package com.Mirra.eCommerce.Service.Orders;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Orders.OrderStatus;
import com.Mirra.eCommerce.Repository.Orders.OrderItemsRepository;
import com.Mirra.eCommerce.Repository.Orders.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderItemsRepository orderItemRepo;

    @Override
    public void saveOrder(Order order) {
        orderRepo.save(order);
    }
    @Override
    public List<Order> getAllOrders() {
        // Use the orderRepository to fetch all orders
        return orderRepo.findAll();
    }

    @Override
    public Order getOrderById(int orderId) {
        return orderRepo.findById(orderId).orElse(null);
    }

    @Override
    public List<Order> findOrdersByUserId(int userId) {
        return orderRepo.findByUserId(userId);
    }


    @Override
    public boolean cancelOrder(int orderId) {
        Optional<Order> optionalOrder = orderRepo.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            // Check if the order is in a cancelable state
            if (order.getStatus() == OrderStatus.ORDERED || order.getStatus() == OrderStatus.PACKED) {
                // Update the order status to CANCELED and record history
                order.updateOrderStatus(OrderStatus.CANCELED);

                // Save the updated order
                orderRepo.save(order);

                return true; // Order was successfully canceled
            }else if (order.getStatus() == OrderStatus.CANCELED) {
                // Order is already canceled
                return false;
            }
        }

        return false; // Order cannot be canceled
    }

    @Override
    public OrderItem getOrderItemById(int orderItemId) {
        Optional<OrderItem> orderItemOptional = orderItemRepo.findById(orderItemId);

        // Check if the order item with the given ID exists
        if (orderItemOptional.isPresent()) {
            return orderItemOptional.get();
        } else {
            // Handle the case where the order item is not found
            throw new NoSuchElementException("Order item with ID " + orderItemId + " not found");
        }
    }







}
