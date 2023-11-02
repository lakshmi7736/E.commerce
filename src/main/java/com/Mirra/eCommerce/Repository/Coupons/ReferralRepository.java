package com.Mirra.eCommerce.Repository.Coupons;

import com.Mirra.eCommerce.Models.Coupons.Referral;
import com.Mirra.eCommerce.Models.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralRepository extends JpaRepository<Referral,Integer> {

    boolean existsByCode(String code);

    boolean existsByUserId (int userId);

    Referral findByCode(String code);

    Referral findCodeByUser(User user);
}
