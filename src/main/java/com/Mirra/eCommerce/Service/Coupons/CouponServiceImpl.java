package com.Mirra.eCommerce.Service.Coupons;

import com.Mirra.eCommerce.Models.Coupons.Coupon;
import com.Mirra.eCommerce.Repository.Coupons.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService{

    @Autowired
    private CouponRepository couponRepo;
//    @Override
//    public String generateCouponCode() {
//        // Generate a random UUID and replace "-" with empty string
//        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//        // Take the first 6 characters from the UUID
//        String couponCode = uuid.substring(0, 6).toUpperCase();
//        return couponCode;
//    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepo.findAll();    }

    @Override
    public Coupon createCoupon(Coupon coupon) {

        return couponRepo.save(coupon);
    }

    @Override
    public boolean isCouponCodeExists(String code) {
        return couponRepo.existsByCode(code);
    }

    @Override
    public List<Coupon> getCouponsInRange(Double minRange, Double maxRange) {

        System.out.println("min"+minRange+" "+"max"+maxRange);
        List<Coupon>  c= couponRepo.findByMinPurchaseAmtBetween(minRange, maxRange);
        if(c.isEmpty()){
            System.out.println("is empty");
        }
        for(Coupon coupon:c){
            System.out.println("form service"+coupon.getCode());
        }
        return c;
    }

    @Override
    public List<Coupon> getCouponsForUser() {
        LocalDate currentDate = LocalDate.now();

        // Fetch all coupons from the repository
        List<Coupon> allCoupons = couponRepo.findAll();

        // Filter the coupons to keep non-expired and null expiration date coupons
        return allCoupons.stream()
                .filter(coupon -> coupon.getExpirationDate() == null || !coupon.getExpirationDate().isBefore(currentDate) ||coupon.getMinPurchaseAmt()==0)
                .collect(Collectors.toList());
    }

    @Override
    public Coupon findByCode(String code) {
        System.out.println("inside service" + code);
        Coupon coupon = couponRepo.findByCode(code);

        if (coupon != null) {
            System.out.println("from repo" + coupon.getCode());
        } else {
            System.out.println("Coupon with code " + code + " not found");
        }

        return coupon;
    }


}
