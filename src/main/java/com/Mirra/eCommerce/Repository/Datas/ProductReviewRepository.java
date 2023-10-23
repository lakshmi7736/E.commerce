package com.Mirra.eCommerce.Repository.Datas;

import com.Mirra.eCommerce.Models.datas.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview,Long> {

    // Custom method to find reviews by product ID
    List<ProductReview> findByProductId(Long productId);
}