package com.Mirra.eCommerce.Service.Category;

import com.Mirra.eCommerce.DTO.CategoryDto;
import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Repository.Datas.CategoryRepository;
import com.Mirra.eCommerce.Repository.Datas.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void saveCategory(CategoryDto categoryDto) {
        byte[] imageData = null;
        if (categoryDto.getImageData() != null) {
            imageData = Base64.getDecoder().decode(categoryDto.getImageData());
        }

        Category category = Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .image(imageData)
                .active(true)
                .build();
        categoryRepository.save(category);
    }

    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public boolean categoryExistsByName(String name) {
        return categoryRepository.existsByName(name);
    }



    @Override
    public Category findById(Long id) {

        return categoryRepository.findById(id).get();
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }




    @Override
    public Category updateCategory(Long id, Category updatedCategory) {
        Category existingCategory = categoryRepository.findById(id).orElse(null);

        if (existingCategory != null) {
            // Update other properties like name and description
            existingCategory.setName(updatedCategory.getName());
            existingCategory.setDescription(updatedCategory.getDescription());

            // Check if a new image is provided and update it if necessary
            if (updatedCategory.getImage() != null) {
                existingCategory.setImage(updatedCategory.getImage());
            }
            // Get all products under the existing category
            List<Product> products = productRepository.findByCategory_Id(existingCategory.getId());

            // Update discountPrice and expirationDate for each product
            for (Product product : products) {

                    product.setActualPrice(product.getMyPrice());

                productRepository.save(product);
            }

            categoryRepository.save(existingCategory);

            return existingCategory;
        } else {
            return null;
        }
    }


}
