package com.Mirra.eCommerce.Service.Checkout;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartOrderItemsServiceImpl implements CartOrderItemsService{


    @Override
    public void createOrderItems(Order order, List<AddToCart> cartItems) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (AddToCart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProducts());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProducts().getMyPrice());
            orderItem.setActualPrice(cartItem.getProducts().getActualPrice());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
    }

    @Override
    public void createOrderItems(Order order, AddToCart cart) {

        List<OrderItem> orderItems = new ArrayList<>();

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(cart.getProducts());
        orderItem.setQuantity(cart.getQuantity());
        orderItem.setPrice(cart.getProducts().getMyPrice());
        orderItem.setActualPrice(cart.getProducts().getActualPrice());
        orderItems.add(orderItem);

        order.setOrderItems(orderItems);
    }
}
