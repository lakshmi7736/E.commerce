package com.Mirra.eCommerce.Repository.Offer;

import com.Mirra.eCommerce.Models.datas.CategoryOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryOfferRepo extends JpaRepository<CategoryOffer,Integer> {

    CategoryOffer findCategoryOfferExistBycategoryId(Long categoryId);

    List<CategoryOffer> findByCategoryId(Long categoryId);

    @Query("SELECT co FROM CategoryOffer co WHERE co.expirationDate > current_timestamp")
    List<CategoryOffer> nonExpiredCategoryOffer();

}
