package com.Mirra.eCommerce.Repository.Coupons;

import com.Mirra.eCommerce.Models.Coupons.ReferralPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralPointsRepository extends JpaRepository<ReferralPoints,Integer> {

    @Query("SELECT b FROM ReferralPoints b")
    ReferralPoints findAnyReferralPoints();

}
