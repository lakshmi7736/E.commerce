package com.Mirra.eCommerce.Service.Orders;

import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Repository.Orders.OrderItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemsAdditionalServiceImpl implements OrderItemsAdditionalService{





    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Override
    public List<OrderItem>  getDeliveredOrders() {

        List<OrderItem> orderItems= orderItemsRepository.findByReturnStatusAndDeliveredOrder();

        return orderItems;
    }



    @Override
    public List<OrderItem> getDeliveredOrdersByDay(LocalDate date) {
        List<OrderItem> deliveredOrders = getDeliveredOrders();
        return deliveredOrders.stream()
                .filter(order -> order.getOrder().getOrderDate().toLocalDate().equals(date))
                .toList();
    }

    @Override
    public List<OrderItem> getDeliveredOrdersByWeek(LocalDate weekStart, LocalDate weekEnd) {
        List<OrderItem> deliveredOrders = getDeliveredOrders();
        return deliveredOrders.stream()
                .filter(order -> !order.getOrder().getOrderDate().isBefore(weekStart.atStartOfDay()) && !order.getOrder().getOrderDate().isAfter(weekEnd.atStartOfDay()))
                .collect(Collectors.toList());
    }


    @Override
    public List<OrderItem> getDeliveredOrdersByMonth(YearMonth yearMonth) {
        List<OrderItem> deliveredOrders = getDeliveredOrders();
        return deliveredOrders.stream()
                .filter(order -> YearMonth.from(order.getOrder().getOrderDate()).equals(yearMonth))
                .collect(Collectors.toList());
    }


    @Override
    public BigDecimal getSumOfPurchaseTotalDeliveredOrdersByYear(int year) {
        List<OrderItem> deliveredOrders = getDeliveredOrders();
        return deliveredOrders.stream()
                .filter(order -> order.getOrder().getOrderDate().toLocalDate().getYear() == year)
                .map(order -> order.getOrder().getPurchaseTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public List<OrderItem> getDeliveredOrdersByYear(int year) {
        List<OrderItem> deliveredOrders = getDeliveredOrders();
        return deliveredOrders.stream()
                .filter(order -> order.getOrder().getOrderDate().toLocalDate().getYear() == year)
                .toList();
    }

    @Override
    public List<OrderItem> getDeliveredOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        List<OrderItem> deliveredOrders = getDeliveredOrders();

        return deliveredOrders.stream()
                .filter(order -> {
                    LocalDate orderDate =order.getOrder().getOrderDate().toLocalDate();
                    return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }


}
