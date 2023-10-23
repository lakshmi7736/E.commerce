package com.Mirra.eCommerce.Service.Product;

import com.Mirra.eCommerce.Models.datas.ProductReview;

import java.util.List;

public interface ProductReviewService {
    public void saveReview(ProductReview review);
    public List<ProductReview> getReviewsByProductId(Long productId);
}
