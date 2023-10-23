package com.Mirra.eCommerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;

    private String description;
    
    private String imageData;

    private BigDecimal discountPrice;

    private LocalDate expirationDate;
}