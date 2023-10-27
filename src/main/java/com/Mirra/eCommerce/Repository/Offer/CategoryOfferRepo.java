package com.Mirra.eCommerce.Repository.Offer;

import com.Mirra.eCommerce.Models.datas.CategoryOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CategoryOfferRepo extends JpaRepository<CategoryOffer,Integer> {


    CategoryOffer findByCategoryId(Long categoryId);
    // Define a custom query method to find non-expired CategoryOffers
    @Query("SELECT co FROM CategoryOffer co WHERE co.expirationDate >= :currentDate")
    List<CategoryOffer> findNonExpired(@Param("currentDate") LocalDate currentDate);

}
