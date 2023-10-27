package com.Mirra.eCommerce.Service.Product;

import com.Mirra.eCommerce.DTO.ProductOfferDto;
import com.Mirra.eCommerce.Models.datas.ProductOffer;

import java.util.List;

public interface ProductOfferService {

    public void saveProductOffer(Long productId,ProductOfferDto productOfferDto) ;

    ProductOffer findByProductId(Long productId);

    ProductOffer findById(int id);

    void deleteProductOfferById(int id);


}
