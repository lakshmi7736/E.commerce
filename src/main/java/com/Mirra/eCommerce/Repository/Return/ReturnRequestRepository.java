package com.Mirra.eCommerce.Repository.Return;

import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Returns.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnRequestRepository extends JpaRepository<ReturnRequest,Integer> {

    List<ReturnRequest> findByOrderItem_Id(int orderItemId);
    ReturnRequest findByOrderItem(OrderItem orderItem);
}
