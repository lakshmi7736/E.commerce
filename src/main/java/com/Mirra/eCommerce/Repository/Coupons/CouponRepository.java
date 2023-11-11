package com.Mirra.eCommerce.Repository.Coupons;

import com.Mirra.eCommerce.Models.Coupons.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Integer> {

    boolean existsByCode(String code);

    List<Coupon> findByMinPurchaseAmtBetween(double minRange, double maxRange);

    List<Coupon> findByExpirationDateAfter(LocalDate currentDate);

    @Query("SELECT c FROM Coupon c WHERE UPPER(TRIM(c.code)) = UPPER(TRIM(:code))")
    Coupon findByCodeIgnoreCase(@Param("code") String code);
}
