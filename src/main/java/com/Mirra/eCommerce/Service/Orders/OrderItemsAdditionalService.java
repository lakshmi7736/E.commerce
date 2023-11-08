package com.Mirra.eCommerce.Service.Orders;

import com.Mirra.eCommerce.Models.Orders.OrderItem;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface OrderItemsAdditionalService {

    public List<OrderItem> getDeliveredOrders();

    public List<OrderItem> getDeliveredOrdersByDay(LocalDate date);

    public List<OrderItem> getDeliveredOrdersByWeek(LocalDate weekStart, LocalDate weekEnd);

    public List<OrderItem> getDeliveredOrdersByMonth(YearMonth yearMonth);

    public List<OrderItem> getDeliveredOrdersByYear(int year);

    public List<OrderItem> getDeliveredOrdersByDateRange(LocalDate startDate, LocalDate endDate);
}
