package com.Mirra.eCommerce.Service.Category;

import com.Mirra.eCommerce.DTO.CategoryOfferDto;
import com.Mirra.eCommerce.Models.ServiceImages.Banner;
import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.CategoryOffer;
import com.Mirra.eCommerce.Repository.Datas.CategoryRepository;
import com.Mirra.eCommerce.Repository.Offer.CategoryOfferRepo;
import com.Mirra.eCommerce.Service.Category.CategoryOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryOfferServiceImpl implements CategoryOfferService {

    @Autowired
    private CategoryOfferRepo categoryOfferRepo;

    @Autowired
    private CategoryRepository categoryRepository;
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
        CategoryOffer existingOffer = categoryOfferRepo.findByCategoryId(categoryId);
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

    @Override
    public void saveCategoryOffer(Long categoryId, CategoryOfferDto categoryOfferDto) {
        if (categoryOfferDto == null) {
            throw new IllegalArgumentException("CategoryOfferDto is null");
        }
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID is null");
        }

        Optional<Category> category = categoryRepository.findById(categoryId);
        Category categoryEntity = category.orElseThrow(() -> new IllegalArgumentException("Category not found"));

        byte[] imageData = null;
        if (categoryOfferDto.getImageData() != null) {
            imageData = Base64.getDecoder().decode(categoryOfferDto.getImageData());
        }

        CategoryOffer existingOffer = categoryOfferRepo.findByCategoryId(categoryId);
        if (existingOffer != null) {
            categoryOfferRepo.delete(existingOffer);
        }

        CategoryOffer offer = CategoryOffer.builder()
                .category(categoryEntity)
                .discountPrice(categoryOfferDto.getDiscountPrice())
                .expirationDate(categoryOfferDto.getExpirationDate())
                .image(imageData)
                .saleEvent(categoryOfferDto.getSaleEvent())
                .build();

        try {
            categoryOfferRepo.save(offer);
        } catch (DataAccessException e) {
            // Handle the database save operation exception
            // You can log the error and rethrow a custom exception or return an error message.
        }
    }

    @Override
    public CategoryOffer findByCategoryId(Long categoryId) {
        return categoryOfferRepo.findByCategoryId(categoryId);
    }

    @Override
    public List<CategoryOffer> findAll() {
        LocalDate currentDate = LocalDate.now();
        List<CategoryOffer> nonExpiredOffers = categoryOfferRepo.findNonExpired(currentDate);
        return nonExpiredOffers;
    }


    @Override
    public void deleteByCategoryOfferId(int categoryOfferId) {
        categoryOfferRepo.deleteById(categoryOfferId);
    }

    @Override
    public CategoryOffer findById(int id) {
        return categoryOfferRepo.findById(id).get();
    }




}

