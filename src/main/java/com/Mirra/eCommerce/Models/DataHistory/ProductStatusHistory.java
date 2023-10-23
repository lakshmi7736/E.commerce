package com.Mirra.eCommerce.Models.DataHistory;

import com.Mirra.eCommerce.Models.datas.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    @JoinColumn(name = "products_id")
    private Product products;

    private int stock;


    private LocalDateTime changeTimestamp;
}
