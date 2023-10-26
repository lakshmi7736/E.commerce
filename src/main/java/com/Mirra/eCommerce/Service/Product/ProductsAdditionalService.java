package com.Mirra.eCommerce.Service.Product;

import com.Mirra.eCommerce.Models.datas.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductsAdditionalService {

    public BigDecimal findMaxActualPrice();

    public List<Product> findProductsUnderPrice(BigDecimal minPrice, BigDecimal maxPrice);

    public List<Product> searchProducts(String alphabets);
}
