package com.Mirra.eCommerce.Service.OrderManagement;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Orders.OrderStatus;
import com.Mirra.eCommerce.Models.Returns.ReturnRequest;
import com.Mirra.eCommerce.Repository.Orders.OrderItemsRepository;
import com.Mirra.eCommerce.Repository.Return.ReturnRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnRequestServiceImpl implements ReturnRequestService{

    @Autowired
    private ReturnRequestRepository returnRequestRepository;

    @Autowired
    private OrderItemsRepository orderItemRepo;


    @Override
    public ReturnRequest createReturnRequest(ReturnRequest returnRequest) {
        // Assuming that returnRequest contains the OrderItem associated with the return

        OrderItem orderItem = returnRequest.getOrderItem();

        // Check if the order item is eligible for return based on your business rules
        if (!isEligibleForReturn(orderItem.getOrder())) {
            throw new IllegalArgumentException("This item is not eligible for return.");
        }


        // Save the return request
        return returnRequestRepository.save(returnRequest);
    }

    @Override
    public List<ReturnRequest> getReturnRequestsByOrderItemId(int orderItemId) {
        return returnRequestRepository.findByOrderItem_Id(orderItemId);
    }

    @Override
    public ReturnRequest getReturnRequestByOrderItem(OrderItem orderItem) {
        return returnRequestRepository.findByOrderItem(orderItem);
    }

//    @Override
//    public List<ReturnRequest> getReturnRequestsByOrderItem(OrderItem orderItem) {
//        return returnRequestRepository.findListOfOrderItem(orderItem);
//    }
//    @Override
//    public List<ReturnRequest> getReturnRequestsByOrderItems(List<OrderItem> orderItems) {
//        List<ReturnRequest> returnRequests = new ArrayList<>();
//
//        for (OrderItem orderItem : orderItems) {
//            // Assuming you have a method to retrieve return requests by order item
//            List<ReturnRequest> requestsForOrderItem = getReturnRequestsByOrderItem(orderItem);
//            returnRequests.addAll(requestsForOrderItem);
//        }
//
//        return returnRequests;
//    }
//


    private boolean isEligibleForReturn(Order order) {
        // Check if the order's status allows returns (modify this based on your business rules)
        if (!isOrderStatusEligibleForReturn(order.getStatus())) {
            return false;
        }

        // Add more conditions based on your business rules

        return true; // If all conditions are met, the order is eligible for return
    }

    private boolean isOrderStatusEligibleForReturn(OrderStatus orderStatus) {
        // Check your business rules to determine if the order's status allows returns.
        // For example, you might allow returns only if the order status is "DELIVERED."

        return orderStatus == OrderStatus.DELIVERED;
    }


}
