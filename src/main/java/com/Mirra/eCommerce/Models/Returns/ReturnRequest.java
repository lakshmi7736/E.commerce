package com.Mirra.eCommerce.Models.Returns;

import com.Mirra.eCommerce.Models.Orders.OrderItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    private String reason;

    private int returnQuantity;


    @Enumerated(EnumType.STRING)
    private ReturnStatus status;

}
