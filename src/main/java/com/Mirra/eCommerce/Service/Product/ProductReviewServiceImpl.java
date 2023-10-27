package com.Mirra.eCommerce.Service.Product;

import com.Mirra.eCommerce.Models.datas.ProductReview;
import com.Mirra.eCommerce.Repository.Datas.ProductReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductReviewServiceImpl implements ProductReviewService{

    @Autowired
    private ProductReviewRepository productReviewRepository;
    @Override
    public void saveReview(ProductReview review) {
        productReviewRepository.save(review);
    }

    @Override
    public List<ProductReview> getReviewsByProductId(Long productId) {
        // Use the productReviewRepository to find reviews by product ID
        return productReviewRepository.findByProductId(productId);
    }
}