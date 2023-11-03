package com.Mirra.eCommerce.Repository.Orders;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUserId(int userId);


    @Query("SELECT o.user, COUNT(o) AS orderCount " +
            "FROM Order o " +
            "WHERE o.status = 'DELIVERED' " +
            "GROUP BY o.user " +
            "ORDER BY orderCount DESC")
    List<Object[]> findMostOrderedUser();


    List<Order> findByStatus(OrderStatus status);
}
