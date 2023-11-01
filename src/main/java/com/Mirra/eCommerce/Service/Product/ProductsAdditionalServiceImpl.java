package com.Mirra.eCommerce.Service.Product;

import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Repository.Datas.ProductRepository;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ProductsAdditionalServiceImpl implements ProductsAdditionalService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SerializeAndDeserialize serializeAndDeserialize;


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

    @Override
    public  List<String> getEncodedImagesList(List<Product> products) throws IOException, ClassNotFoundException {
        List<String> encodedImagesList = new ArrayList<>();
        for (Product product : products) {
            List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());
            String encodedImage = Base64.getEncoder().encodeToString(imageDataList.get(0));
            encodedImagesList.add(encodedImage);
        }
        return encodedImagesList;
    }

}
