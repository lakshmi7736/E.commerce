package com.Mirra.eCommerce.Service.Checkout;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Users.Address;
import com.Mirra.eCommerce.Models.Users.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreateOrderServiceImpl implements CreateOrderService{
    @Override
    public Order createOrder(User user, Address selectedAddress, BigDecimal grandTotal,BigDecimal subTotal) {
        System.out.println("grandTotal "+grandTotal);
        Order order = new Order();
        order.setUser(user);
        order.setAddress(selectedAddress);
        order.setGranTotal(subTotal);
        order.setPurchaseTotal(grandTotal);
        return order;
    }
}
