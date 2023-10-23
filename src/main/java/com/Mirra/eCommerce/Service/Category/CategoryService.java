package com.Mirra.eCommerce.Service.Category;

import com.Mirra.eCommerce.DTO.CategoryDto;
import com.Mirra.eCommerce.Models.datas.Category;

import java.util.List;

public interface CategoryService {

    //to save category
    void saveCategory(CategoryDto categoryDto);

    void saveCategory(Category category);

    //to get list of all categories
    List<Category> getAllCategories();

    //to confirm no category with same name.
    boolean categoryExistsByName(String name);

    Category findById(Long id);

    void deleteCategory(Long id);

    Category updateCategory(Long id, Category category);
}
