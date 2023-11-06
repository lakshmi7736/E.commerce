package com.Mirra.eCommerce.Service.Checkout;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Users.Related.AddToCart;

import java.util.List;

public interface CartOrderItemsService {

    public void createOrderItems(Order order, List<AddToCart> cartItems);

    public void createOrderItems(Order order, AddToCart cart);
}
