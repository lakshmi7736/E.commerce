package com.Mirra.eCommerce.Repository.Orders;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Returns.ReturnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findByOrderIdAndReturnStatus(int orderId, ReturnStatus returnStatus);



    @Query("SELECT oi FROM OrderItem oi " +
            "JOIN oi.order o " +
            "WHERE oi.returnStatus = 'APPROVED' AND o.status = 'DELIVERED'")
    List<OrderItem> findByReturnStatusAndDeliveredOrder();


}
