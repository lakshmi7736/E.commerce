package com.Mirra.eCommerce.Service.Orders;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Orders.OrderStatus;
import com.Mirra.eCommerce.Models.Returns.ReturnStatus;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Repository.Orders.OrderItemsRepository;
import com.Mirra.eCommerce.Repository.Orders.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderAdditionalServiceImpl implements OrderAdditionalService{


    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Override
    public User findMostOrderedUser() {
        List<Object[]> result = orderRepo.findMostOrderedUser();
        if (!result.isEmpty()) {
            Object[] row = result.get(0);
            return (User) row[0];
        }
        return null;
    }

    @Override
    public List<Order> getDeliveredOrders() {

        List<Order> orders= orderRepo.findByStatus(OrderStatus.DELIVERED);

        return  orders;

    }



    @Override
    public List<Order> getDeliveredOrdersByDay(LocalDate date) {
        List<Order> deliveredOrders = getDeliveredOrders();
        return deliveredOrders.stream()
                .filter(order -> order.getOrderDate().toLocalDate().equals(date))
                .toList();
    }

    @Override
    public List<Order> getDeliveredOrdersByWeek(LocalDate weekStart, LocalDate weekEnd) {
        List<Order> deliveredOrders = getDeliveredOrders();
        return deliveredOrders.stream()
                .filter(order -> !order.getOrderDate().isBefore(weekStart.atStartOfDay()) && !order.getOrderDate().isAfter(weekEnd.atStartOfDay()))
                .collect(Collectors.toList());
    }


    @Override
    public List<Order> getDeliveredOrdersByMonth(YearMonth yearMonth) {
        List<Order> deliveredOrders = getDeliveredOrders();
        return deliveredOrders.stream()
                .filter(order -> YearMonth.from(order.getOrderDate()).equals(yearMonth))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> getDeliveredOrdersByYear(int year) {
        List<Order> deliveredOrders = getDeliveredOrders();
        return deliveredOrders.stream()
                .filter(order -> order.getOrderDate().toLocalDate().getYear() == year)
                .toList();
    }

    @Override
    public List<Order> getDeliveredOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Order> deliveredOrders = getDeliveredOrders();
        System.out.println(startDate+" "+endDate +"inside service");

        return deliveredOrders.stream()
                .filter(order -> {
                    LocalDate orderDate = order.getOrderDate().toLocalDate(); // Assuming getOrderDate() returns a LocalDateTime or similar.
                    return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }
}
