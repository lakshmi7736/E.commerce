package com.Mirra.eCommerce.Repository.Orders;

import com.Mirra.eCommerce.Models.Orders.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory,Integer> {

    List<OrderStatusHistory> findByOrderId(int orderId);
}
