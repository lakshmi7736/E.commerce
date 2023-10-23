package com.Mirra.eCommerce.Repository.Offer;

import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.CategoryOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryOfferRepo extends JpaRepository<CategoryOffer,Integer> {

//    CategoryOffer findOfferExist(Category categoryId);

}
