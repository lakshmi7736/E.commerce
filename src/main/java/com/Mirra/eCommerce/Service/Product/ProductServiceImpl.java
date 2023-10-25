package com.Mirra.eCommerce.Service.Product;

import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Repository.Datas.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    public ProductRepository productRepository;
    @Transactional
    @Override
    public void saveProduct(Product products) {
        productRepository.save(products);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        Optional<Product> optionalUser = productRepository.findById(id);
        return optionalUser.orElse(null);
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }



    @Override
    public List<Product> getProductsBySubCategoryId(Long subCategoryId) {
        // Assuming you have a JPA entity class called "Product"
        // and a corresponding repository "ProductRepository"
        return productRepository.findBySubCategory_Id(subCategoryId);
    }

    @Override
    public Product deleteProduct(Product product) {
        product.setActive(false);
        return productRepository.save(product);
    }

    @Override
    public void saveAllProducts(List<Product> products) {
        productRepository.saveAll(products);
    }

    @Override
    public BigDecimal findMaxActualPrice() {
        return productRepository.findMaxActualPrice();
    }

    @Override
    public List<Product> findProductsUnderPrice(BigDecimal minPrice,BigDecimal maxPrice) {
        return productRepository.findByActualPriceBetween(minPrice,maxPrice);
    }

}
