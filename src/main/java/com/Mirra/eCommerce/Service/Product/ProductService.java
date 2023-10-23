package com.Mirra.eCommerce.Service.Product;

import com.Mirra.eCommerce.Models.datas.Product;

import java.util.List;

public interface ProductService {

    public void saveProduct(Product products);

    public List<Product> getAllProducts();

    public Product getProductById(Long id);

    public List<Product> getProductsByCategoryId(Long categoryId);

    public List<Product> getProductsBySubCategoryId(Long subCategoryId);

    public Product deleteProduct(Product product);

    public void saveAllProducts(List<Product> products);

}
