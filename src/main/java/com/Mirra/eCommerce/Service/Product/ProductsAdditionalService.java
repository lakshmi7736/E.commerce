package com.Mirra.eCommerce.Service.Product;

import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public interface ProductsAdditionalService {



    public BigDecimal findMaxActualPrice();

    public List<Product> findProductsUnderPrice(BigDecimal minPrice, BigDecimal maxPrice);

    public List<Product> searchProducts(String alphabets);

     List<String> getEncodedImagesList(List<Product> products) throws IOException, ClassNotFoundException;

}
