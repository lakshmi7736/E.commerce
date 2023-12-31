package com.Mirra.eCommerce.Service.Product;

import com.Mirra.eCommerce.DTO.ProductOfferDto;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Models.datas.ProductOffer;
import com.Mirra.eCommerce.Repository.Offer.ProductOfferRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOfferServiceImpl implements ProductOfferService{

    @Autowired
    private ProductOfferRepo productOfferRepo;

    @Autowired
    private ProductService productsService;
    @Override
    public void saveProductOffer(Long productId,ProductOfferDto productOfferDto) {
        // Validate the input data (e.g., product, expirationDate, etc.)
        if (productOfferDto == null || productOfferDto.getProductId() == null) {
            throw new IllegalArgumentException("Invalid ProductOfferDto");
        }
        // Delete the existing offer with the same category ID, if it exists
        ProductOffer existingOffer = productOfferRepo.findProductOfferExistByproductId(productId);
        if (existingOffer != null) {
            productOfferRepo.delete(existingOffer);
        }

        Product product=productsService.getProductById(productId);

        // Create and save the new CategoryOffer
        ProductOffer offer = ProductOffer.builder()
                .discountPrice(productOfferDto.getDiscountPrice())
                .expirationDate(productOfferDto.getExpirationDate())
                .product(product)
                .build();

        productOfferRepo.save(offer);
    }

    @Override
    public ProductOffer findByProductId(Long productId) {
        return productOfferRepo.findProductOfferExistByproductId(productId);
    }

    @Override
    public ProductOffer findById(int id) {
        return productOfferRepo.findById(id).get();
    }

    @Override
    public void deleteProductOfferById(int id) {
        productOfferRepo.deleteById(id);
    }


}
