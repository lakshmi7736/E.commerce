package com.Mirra.eCommerce.Service.Checkout;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Users.Address;
import com.Mirra.eCommerce.Models.Users.User;

import java.math.BigDecimal;

public interface CreateOrderService {

    public Order createOrder(User user, Address selectedAddress, BigDecimal grandTotal, BigDecimal subTotal);
}
