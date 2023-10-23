package com.Mirra.eCommerce.DTO;

import com.Mirra.eCommerce.Models.datas.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryOfferDto {

    int id;

    private String saleEvent;

    private BigDecimal discountPrice;

    private LocalDate expirationDate;

    private String imageData;

    private Category categoryId;
}
