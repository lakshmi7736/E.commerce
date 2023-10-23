package com.Mirra.eCommerce.Models.datas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOffer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private BigDecimal discountPrice;

    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


        public void checkExpirationDate() {
        LocalDate currentDate = LocalDate.now();
        if (expirationDate != null && currentDate.isAfter(expirationDate)) {
            // If the expiration date is in the past, set discountPrice to 0
            discountPrice = null;
        }
    }

}
