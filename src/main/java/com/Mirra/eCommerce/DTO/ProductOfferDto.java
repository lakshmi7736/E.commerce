package com.Mirra.eCommerce.DTO;

import com.Mirra.eCommerce.Models.datas.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOfferDto {
    int id;
    private BigDecimal discountPrice;

    private LocalDate expirationDate;

    private String imageData;

    private Product productId;
}


