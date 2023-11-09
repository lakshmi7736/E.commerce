package com.Mirra.eCommerce.Controller.AdminController.CouponsController;

import com.Mirra.eCommerce.Models.Coupons.Coupon;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Service.Checkout.CalculationService;
import com.Mirra.eCommerce.Service.Coupons.CouponService;
import com.Mirra.eCommerce.Service.User.Related.CartlistService;
import com.Mirra.eCommerce.Service.User.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;


    @Autowired
    private UserService userService;

    @Autowired
    private CartlistService cartService;

    @Autowired
    private CalculationService calculationService;

    @GetMapping
    public String showCoupons(Model model) {
        // Retrieve a list of coupons from the database
        List<Coupon> coupons = couponService.getAllCoupons();
        model.addAttribute("coupons", coupons);
        return "Admin/Coupons/Coupon-list";
    }

    @GetMapping("/create")
    public String showCreateCouponForm(Model model) {
        // Display a form for creating a new coupon
        model.addAttribute("coupon", new Coupon());
        return "Admin/Coupons/Create-coupons";
    }

    @PostMapping("/create")
    public String createCoupon(@ModelAttribute Coupon coupon, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create-coupon"; // Return to the registration form with validation errors.
        }

        // Check if the coupon code already exists
        if (couponService.isCouponCodeExists(coupon.getCode())) {
            bindingResult.rejectValue("code", "duplicate.coupon.code", "Coupon code already exists");
            return "create-coupon";
        }
        else if (coupon.getMinPurchaseAmt()<500 ) {
            bindingResult.rejectValue("minPurchaseAmt", "duplicate.coupon.minPurchaseAmt", "Minimum purchase amount is 1200");
            return "create-coupon";
        }

        else if (coupon.getDiscountAmount()>5000) {
            bindingResult.rejectValue("discountAmount", "duplicate.coupon.discountAmount", "Discount amount should not exceeds than 5000");
            return "create-coupon";
        }
        else if (coupon.getExpirationDate()==null) {
            bindingResult.rejectValue("expirationDate", "duplicate.coupon.expirationDate", "Expiry Date required");
            return "create-coupon";
        }

        // Create a new coupon and save it to the database
        couponService.createCoupon(coupon);
        return "redirect:/admin/coupons";
    }






}

