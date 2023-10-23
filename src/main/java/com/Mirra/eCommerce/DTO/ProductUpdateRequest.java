package com.Mirra.eCommerce.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductUpdateRequest {
    private String name;
    private String description;
    private BigDecimal productDiscountPrice;
    private BigDecimal actualPrice;
    private LocalDate expirationDate;
    private int updateStock;
    private Long subCategoryId;
    private MultipartFile[] newImages;

}
