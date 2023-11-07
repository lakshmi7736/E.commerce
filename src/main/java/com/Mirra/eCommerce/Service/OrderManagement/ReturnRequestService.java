package com.Mirra.eCommerce.Service.OrderManagement;

import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Returns.ReturnRequest;

import java.util.List;

public interface ReturnRequestService {

    public ReturnRequest createReturnRequest(ReturnRequest returnRequest);

    public List<ReturnRequest> getReturnRequestsByOrderItemId(int orderItemId);

    public ReturnRequest getReturnRequestByOrderItem(OrderItem orderItem);
}
