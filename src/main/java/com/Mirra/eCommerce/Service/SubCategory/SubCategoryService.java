package com.Mirra.eCommerce.Service.SubCategory;

import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.SubCategory;

import java.util.List;

public interface SubCategoryService {
    public void saveSubCategory(SubCategory subCategory);

    public List<SubCategory> getAllSubCategories();

    public boolean subCategoryExistsByNameAndParentCategory(String name, Category parentCategory);

    public List<SubCategory> getSubcategoriesByCategoryId(Long categoryId);

    public SubCategory findById(Long id);

    public void deleteSubCategory(Long id);

    public SubCategory updateSubCategory(Long id, SubCategory subCategory);

    public SubCategory getSubCategoryById(Long subCategoryId);

}
