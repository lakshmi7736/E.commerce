package com.Mirra.eCommerce.Service.Coupons;

import com.Mirra.eCommerce.Models.Coupons.Coupon;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService {
    public List<Coupon> getAllCoupons();
    public Coupon createCoupon(Coupon coupon);

    public boolean isCouponCodeExists(String code);

    public List<Coupon> getCouponsInRange(Double minRange, Double maxRange);

    public List<Coupon> getCouponsForUser();

    public Coupon findByCode(String code);
}
