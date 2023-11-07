package com.Mirra.eCommerce.Controller.AdminController.OrderController;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderStatus;
import com.Mirra.eCommerce.Models.Orders.OrderStatusHistory;
import com.Mirra.eCommerce.Service.Orders.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Controller
@RequestMapping("/admin")
public class OrderManagementController {

    @Autowired
    private OrderService orderService;




    @PostMapping("/updateOrderStatus/{orderId}")
    public String updateOrderStatus(
            @PathVariable int orderId,
            @RequestParam("newStatus") OrderStatus newStatus
    ) {
        // Fetch the order by ID
        Order order = orderService.getOrderById(orderId);

        // Create a new status change history entry
        OrderStatusHistory statusHistory = new OrderStatusHistory();
        statusHistory.setOrder(order);
        statusHistory.setNewStatus(newStatus);
        statusHistory.setChangeTimestamp(LocalDateTime.now());

        // Update the order's status
        order.setStatus(newStatus);

        // Add the status change history entry to the order
        if (order.getStatusHistory() == null) {
            order.setStatusHistory(new ArrayList<>());
        }
        order.getStatusHistory().add(statusHistory);

        // Save the updated order
        orderService.saveOrder(order);

        // Redirect back to the checkout page or wherever you want
        return "redirect:/admin/ordersController";
    }



}
