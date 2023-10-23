package com.Mirra.eCommerce.Repository.Datas;

import com.Mirra.eCommerce.Models.datas.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product,Long> {

    List<Product> findByCategory_Id(Long categoryId);

    List<Product> findBySubCategory_Id(Long subCategoryId);
}
