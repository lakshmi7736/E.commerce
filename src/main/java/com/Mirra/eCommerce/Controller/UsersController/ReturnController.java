package com.Mirra.eCommerce.Controller.UsersController;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Orders.OrderStatus;
import com.Mirra.eCommerce.Models.Returns.ReturnRequest;
import com.Mirra.eCommerce.Models.Returns.ReturnStatus;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
import com.Mirra.eCommerce.Service.OrderManagement.ReturnRequestService;
import com.Mirra.eCommerce.Service.Orders.OrderItemService;
import com.Mirra.eCommerce.Service.Orders.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Controller
@RequestMapping("/user/request")
public class ReturnController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SerializeAndDeserialize serializeAndDeserialize;


    @Autowired
    private ReturnRequestService returnRequestService;

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/return/{orderId}")
    public String returnProducts(@PathVariable int orderId, Model model, RedirectAttributes redirectAttributes) throws IOException, ClassNotFoundException {
        // Retrieve the order details, e.g., using orderService.getOrderById(orderId)
        Order order = orderService.getOrderById(orderId);

        // Check if the order status is "Delivered" to allow returns
        if (order.getStatus() == OrderStatus.DELIVERED) {
            model.addAttribute("order", order);

            // Encode product images for each order item and add them to the model
            List<String> encodedImagesList = new ArrayList<>();
            List<OrderItem> returnableOrderItems = new ArrayList<>();

            for (OrderItem orderItem : order.getOrderItems()) {

                List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(orderItem.getProduct().getImageBlob());
                String encodedImage = Base64.getEncoder().encodeToString(imageDataList.get(0));
                encodedImagesList.add(encodedImage);
                returnableOrderItems.add(orderItem);

            }

            model.addAttribute("encodedImagesList", encodedImagesList);
            model.addAttribute("returnableOrderItems", returnableOrderItems);

            return "User/Related/ReturnProduct"; // Display the return page
        } else if (order.getStatus() == OrderStatus.CANCELED) {
            redirectAttributes.addFlashAttribute("orderMessage", "You can't return this order because it is canceled.");
        } else if (order.getStatus() == OrderStatus.RETURNED) {
            redirectAttributes.addFlashAttribute("orderMessage", "You can't return this order because it has already been returned.");
        } else {
            redirectAttributes.addFlashAttribute("orderMessage", "You can't return this order because it is not delivered yet.");
        }

        return "redirect:/user/myOrders";
    }


    @PostMapping("/initiate/{orderItemId}")
    public String initiateReturn(
            @PathVariable int orderItemId,
            @ModelAttribute("reason") String reason,
            @ModelAttribute("returnQuantity") int returnQuantity,
            @RequestParam(value = "raiseComplaint", required = false) boolean raiseComplaint, // Capture the checkbox value
            RedirectAttributes redirectAttributes){
        // Retrieve the OrderItem by its ID
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);

        if (orderItem.getReturnStatus()== ReturnStatus.APPROVED){
            redirectAttributes.addFlashAttribute("message", "Return request is already done.");
        }
        else {

            // Check if a return request already exists for this OrderItem
            ReturnRequest existingReturnRequest = returnRequestService.getReturnRequestByOrderItem(orderItem);

            if (existingReturnRequest == null) {
                // No existing return request, create a new one

                // Create a new return request
                ReturnRequest returnRequest = new ReturnRequest();
                returnRequest.setOrderItem(orderItem);
                returnRequest.setReason(reason);
                returnRequest.setReturnQuantity(returnQuantity);
                // Set the status based on whether the complaint checkbox is checked
                if (raiseComplaint) {
                    returnRequest.setStatus(ReturnStatus.COMPLAINT);
                    orderItem.setReturnStatus(ReturnStatus.COMPLAINT);

                }else {
                    returnRequest.setStatus(ReturnStatus.PENDING); // Initial status is pending
                    // Update the status of the OrderItem to "pending"
                    orderItem.setReturnStatus(ReturnStatus.PENDING);
                }

                // Save the return request
                returnRequestService.createReturnRequest(returnRequest);


                orderItemService.updateOrderItem(orderItem);

                // Redirect with a success message
                redirectAttributes.addFlashAttribute("message", "Return request initiated successfully.");
            } else {
                // An existing return request exists for this OrderItem

                // Check the status of the existing return request
                ReturnStatus existingStatus = existingReturnRequest.getStatus();

                if (existingStatus == ReturnStatus.PENDING) {
                    // Return request is already pending
                    redirectAttributes.addFlashAttribute("message", "Return request is already pending.");
                } else if (existingStatus == ReturnStatus.APPROVED) {
                    // Return request is approved
                    redirectAttributes.addFlashAttribute("message", "Return request is already approved.");
                } else if (existingStatus == ReturnStatus.DENIED) {
                    // Return request is denied
                    redirectAttributes.addFlashAttribute("message", "Return request was denied.");
                }
                else if (existingStatus == ReturnStatus.COMPLAINT) {
                    // Return request is denied
                    redirectAttributes.addFlashAttribute("message", "Return request is already pending.");
                }
            }
        }


        return "redirect:/user/request/return/" + orderItem.getOrder().getId(); // Redirect to the order details page
    }
}
