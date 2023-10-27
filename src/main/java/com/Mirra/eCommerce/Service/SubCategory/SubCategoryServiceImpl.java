package com.Mirra.eCommerce.Service.SubCategory;

import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Models.datas.SubCategory;
import com.Mirra.eCommerce.Repository.Datas.CategoryRepository;
import com.Mirra.eCommerce.Repository.Datas.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService{

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public void saveSubCategory(SubCategory subCategory) {
        subCategoryRepository.save(subCategory);
    }

    @Override
    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }


    @Override
    public boolean subCategoryExistsByNameAndParentCategory(String name, Category parentCategory) {
        return subCategoryRepository.existsByNameAndParentCategory(name, parentCategory);
    }

    @Override
    public List<SubCategory> getSubcategoriesByCategoryId(Long categoryId) {
        return subCategoryRepository.findByParentCategoryId(categoryId);
    }

    @Override
    public SubCategory findById(Long id) {
        return subCategoryRepository.findById(id).get();
    }

    @Override
    public void deleteSubCategory(Long id) {
        subCategoryRepository.deleteById(id);
    }

    @Override
    public SubCategory updateSubCategory(Long id, SubCategory subCategory) {
        SubCategory sr = subCategoryRepository.findById(id).orElse(null);

        if (sr != null) {
            sr.setName(subCategory.getName());

            // Update the parentCategory if it's not null in the subCategory parameter.
            if (subCategory.getParentCategory() != null) {
                sr.setParentCategory(subCategory.getParentCategory());
            }

            subCategoryRepository.save(sr);
            return sr;
        } else {
            return null;
        }
    }

    @Override
    public SubCategory getSubCategoryById(Long subCategoryId) {
        return subCategoryRepository.findById(subCategoryId).orElse(null);
    }




}
