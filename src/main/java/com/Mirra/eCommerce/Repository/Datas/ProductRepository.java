package com.Mirra.eCommerce.Repository.Datas;

import com.Mirra.eCommerce.Models.datas.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product,Long> {

    List<Product> findByCategory_Id(Long categoryId);

    List<Product> findBySubCategory_Id(Long subCategoryId);

    @Query("SELECT MAX(p.actualPrice) FROM Product p")
    BigDecimal findMaxActualPrice();

    List<Product> findByActualPriceBetween(BigDecimal minPrice,BigDecimal maxPrice);
}
