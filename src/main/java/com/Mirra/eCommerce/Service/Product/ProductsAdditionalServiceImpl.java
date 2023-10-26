package com.Mirra.eCommerce.Service.Product;

import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Repository.Datas.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductsAdditionalServiceImpl implements ProductsAdditionalService {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public BigDecimal findMaxActualPrice() {
        return productRepository.findMaxActualPrice();
    }

    @Override
    public List<Product> findProductsUnderPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByActualPriceBetween(minPrice,maxPrice);
    }

    @Override
    public List<Product> searchProducts(String alphabet) {
        return productRepository.findByNameContaining(alphabet);
    }

}
