package com.Mirra.eCommerce.Models.Orders;

import com.Mirra.eCommerce.Models.Returns.ReturnStatus;
import com.Mirra.eCommerce.Models.datas.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ReturnStatus returnStatus;

}
