package com.Mirra.eCommerce.Controller.AdminController.OrderController;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Orders.OrderStatusHistory;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
import com.Mirra.eCommerce.Service.Orders.OrderService;
import com.Mirra.eCommerce.Service.Orders.OrderStatusHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/user/orders")
public class trackOrderController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderStatusHistoryService orderStatusHistoryService;

    @Autowired
    private SerializeAndDeserialize serializeAndDeserialize;



    @GetMapping("/track-order/{orderId}")
    public String trackOrder(@PathVariable int orderId, Model model) throws IOException, ClassNotFoundException {

        Order order = orderService.getOrderById(orderId);

        if (order != null) {
            List<OrderStatusHistory> orderHistories = orderStatusHistoryService.getOrderStatusHistoryByOrderId(orderId);
            // Encode product images for each order item and add them to the model
            List<String> encodedImagesList = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(orderItem.getProduct().getImageBlob());
                String encodedImage = Base64.getEncoder().encodeToString(imageDataList.get(0));
                encodedImagesList.add(encodedImage);
            }
            model.addAttribute("encodedImagesList", encodedImagesList);

            model.addAttribute("order", order);
            model.addAttribute("orderHistories", orderHistories);

            return "Admin/Order/trackOrder"; // Create a Thymeleaf HTML template for tracking orders
        } else {
            // Handle the case where the order with the specified ID does not exist redirect to home page
            return "redirect:/";
        }
    }
}
