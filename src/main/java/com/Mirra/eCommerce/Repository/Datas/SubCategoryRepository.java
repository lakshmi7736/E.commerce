package com.Mirra.eCommerce.Repository.Datas;

import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Models.datas.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
    boolean existsByNameAndParentCategory(String name, Category parentCategory);
    List<SubCategory> findByParentCategoryId(Long parentCategoryId);

}
