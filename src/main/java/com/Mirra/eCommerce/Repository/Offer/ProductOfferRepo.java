package com.Mirra.eCommerce.Repository.Offer;

import com.Mirra.eCommerce.Models.datas.ProductOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOfferRepo extends JpaRepository<ProductOffer,Integer> {

    ProductOffer findProductOfferExistByproductId(Long productId);
}
