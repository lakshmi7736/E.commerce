package com.Mirra.eCommerce.Service.Category;

import com.Mirra.eCommerce.DTO.CategoryOfferDto;
import com.Mirra.eCommerce.Models.ServiceImages.Banner;
import com.Mirra.eCommerce.Models.datas.CategoryOffer;
import com.Mirra.eCommerce.Repository.Offer.CategoryOfferRepo;
import com.Mirra.eCommerce.Service.Category.CategoryOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class CategoryOfferServiceImpl implements CategoryOfferService {

    @Autowired
    private CategoryOfferRepo categoryOfferRepo;
    @Override
    public void saveCategoryOffer(CategoryOfferDto categoryOfferDto) {
        // Validate the input data (e.g., categoryId, expirationDate, etc.)
        if (categoryOfferDto == null || categoryOfferDto.getCategoryId() == null) {
            throw new IllegalArgumentException("Invalid CategoryOfferDto");
        }

        // Decode image data if it exists
        byte[] imageData = null;
        if (categoryOfferDto.getImageData() != null) {
            imageData = Base64.getDecoder().decode(categoryOfferDto.getImageData());
        }

        Long categoryId= categoryOfferDto.getCategoryId().getId();
        // Delete the existing offer with the same category ID, if it exists
        CategoryOffer existingOffer = categoryOfferRepo.findCategoryOfferExistBycategoryId(categoryId);
        if (existingOffer != null) {
            categoryOfferRepo.delete(existingOffer);
        }

        // Create and save the new CategoryOffer
        CategoryOffer offer = CategoryOffer.builder()
                .category(categoryOfferDto.getCategoryId())
                .discountPrice(categoryOfferDto.getDiscountPrice())
                .expirationDate(categoryOfferDto.getExpirationDate())
                .image(imageData)
                .saleEvent(categoryOfferDto.getSaleEvent())
                .build();
        categoryOfferRepo.save(offer);
    }

}
