package com.Mirra.eCommerce.Service.Product;

import com.Mirra.eCommerce.Models.datas.ProductReview;

import java.util.List;

public interface CalculateAverageRatingService {
    default double calculateAverageRating(List<ProductReview> reviews) {
        if (reviews.isEmpty()) {
            return 0.0;
        }

        int totalRating = 0;
        for (ProductReview review : reviews) {
            totalRating += review.getRating();
        }

        return (double) totalRating / reviews.size();
    }
}
