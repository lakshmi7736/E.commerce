package com.Mirra.eCommerce.Service.Orders;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Users.User;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface OrderAdditionalService {

    public User findMostOrderedUser();

    public List<Order> getDeliveredOrders();

    public List<Order> getDeliveredOrdersByDay(LocalDate date);

    public List<Order> getDeliveredOrdersByWeek(LocalDate weekStart, LocalDate weekEnd);

    public List<Order> getDeliveredOrdersByMonth(YearMonth yearMonth);

    public List<Order> getDeliveredOrdersByYear(int year);

    public List<Order> getDeliveredOrdersByDateRange(LocalDate startDate, LocalDate endDate);

}
