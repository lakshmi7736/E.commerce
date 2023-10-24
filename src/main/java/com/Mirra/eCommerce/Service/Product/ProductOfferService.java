package com.Mirra.eCommerce.Service.Product;

import com.Mirra.eCommerce.DTO.ProductOfferDto;
import com.Mirra.eCommerce.Models.datas.ProductOffer;

public interface ProductOfferService {

    public void saveProductOffer(Long productId,ProductOfferDto productOfferDto) ;

    ProductOffer findByProductId(Long productId);
}
