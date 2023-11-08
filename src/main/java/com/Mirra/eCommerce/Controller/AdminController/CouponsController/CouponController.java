package com.Mirra.eCommerce.Controller.AdminController.CouponsController;

import com.Mirra.eCommerce.Models.Coupons.Coupon;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
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



    @PostMapping("/applyCoupon")
    public String applyCoupon(@RequestParam("couponCode") String couponCode, @RequestParam("cartList") String cartListData, @RequestParam("grandTotal") BigDecimal grandTotal, HttpSession session, Model model, RedirectAttributes ra) {
        System.out.println(couponCode);

        System.out.println("cartListData "+cartListData);
        Coupon coupon = couponService.findByCode(couponCode);


        // Parse the cartListData string back to a List of AddToCart objects
        List<AddToCart> cartList = parseCartListData(cartListData);
        for (AddToCart cart:cartList){
            System.out.println("cart "+cart.getId());
        }

//        BigDecimal minPurchase = BigDecimal.valueOf(coupon.getMinPurchaseAmt());



//        if (grandTotal.compareTo(minPurchase) < 0) {
//            ra.addFlashAttribute("coupon", "Can't apply coupon; didn't reach the minimum purchase.");
//            return "redirect:/checkOut";
//        }

        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");
        User user = userService.findByEmail(jwtResponse.getUsername());

//        boolean couponApplied = applyCouponLogic(couponCode, user.getId(), cartList);
//
//        if (couponApplied) {
//            model.addAttribute("couponApplied", true);
//            ra.addFlashAttribute("coupon", "Added Coupon.");
//        }

        return "redirect:/checkOut";
    }

//    public boolean applyCouponLogic(String couponCode, int userId, List<AddToCart> cart) {
//        Coupon validCoupon = couponService.findByCode(couponCode);
//
//        if (validCoupon != null) {
//            int totalQuantity = 0;
//
//            for (AddToCart toCart : cart) {
//                totalQuantity += toCart.getQuantity();
//            }
//
//            BigDecimal discountAmount = BigDecimal.valueOf(validCoupon.getDiscountAmount());
//            BigDecimal equalDiscountPerQuantity = discountAmount.divide(BigDecimal.valueOf(totalQuantity), 2, RoundingMode.HALF_UP);
//
//            for (AddToCart cartItem : cart) {
//                Product product = cartItem.getProducts();
//                BigDecimal actualPrice = product.getActualPrice();
//                BigDecimal discountedPricePerQuantity = actualPrice.subtract(equalDiscountPerQuantity);
//
//                // No need to compare with zero, just set the calculated value
//                cartItem.setDiscountPrice(discountedPricePerQuantity);
//
//                // Save the updated product (if needed)
//                cartService.updateCart(cartItem);
//            }
//
//            return true;
//        }
//
//        return false;
//    }


    public List<AddToCart> parseCartListData(String cartListData) {
        if (cartListData == null || cartListData.isEmpty()) {
            return Collections.emptyList(); // Return an empty list or handle the empty case as needed
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<AddToCart> cartList = objectMapper.readValue(cartListData, new TypeReference<List<AddToCart>>() {});
            return cartList;
        } catch (IOException e) {
            // Handle parsing errors
            e.printStackTrace(); // You may want to log or handle the error differently
            return Collections.emptyList(); // Return an empty list or handle the error as needed
        }
    }

//    @PostMapping("/applyCoupon")
//    public String applyCoupon(@RequestParam("couponCode") String couponCode,@RequestParam("cartlist")List<AddToCart> cart, @RequestParam("grandTotal") BigDecimal grandTotal, HttpSession session, Model model, RedirectAttributes ra) {
//
//        Coupon coupon = couponService.findByCode(couponCode);
//
//        // Convert minPurchase (double) to a BigDecimal for comparison
//        BigDecimal minPurchase = BigDecimal.valueOf(coupon.getMinPurchaseAmt());
//
//        // Compare BigDecimal using compareTo
//        if (grandTotal.compareTo(minPurchase) < 0) {
//            ra.addFlashAttribute("coupon", "Can't apply coupon; didn't reach the minimum purchase.");
//            return "redirect:/checkOut"; // Redirect to the cart page
//        }
//
//
//        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");
//
//
//        User user = userService.findByEmail(jwtResponse.getUsername());
//
//
//        boolean couponApplied = applyCouponLogic(couponCode,user.getId(),cart); // Replace with your coupon application logic
//
//        if (couponApplied) {
//            model.addAttribute("couponApplied", true);
//            // Add other coupon-related data to the model if needed
//            ra.addFlashAttribute("coupon", "Added Coupon.");
//        }
//
//        // Redirect back to the cart page or wherever you want
//        return "redirect:/checkOut"; // Redirect to the cart page
//    }
//
//
//
//    public boolean applyCouponLogic(String couponCode, int userId,List<AddToCart> cart) {
//        // Assuming you have a list of valid coupon codes and their details
//        // You can replace this with your actual coupon validation logic
//        Coupon validCoupon = couponService.findByCode(couponCode);
//
//        if (validCoupon != null) {
//
//            // Calculate the discount amount based on the coupon's rules
//            BigDecimal discountAmount = BigDecimal.valueOf(validCoupon.getDiscountAmount());
//
//            for (AddToCart toCart:cart){
//                // Calculate the total quantity of products in the cart
//                int totalQuantity = toCart.getQuantity();
//            }
//
//
//
//            // Calculate the equal discount amount per product quantity
//            BigDecimal equalDiscountPerQuantity = discountAmount.divide(BigDecimal.valueOf(totalQuantity), 2, RoundingMode.HALF_UP);
//
//            // Apply the discount to each cart item's product
//            for (AddToCart cartItem : cart) {
//                Product product = cartItem.getProducts();
//
//                // Calculate the discounted price for each quantity
//                BigDecimal actualPrice = product.getActualPrice();
//
////                BigDecimal discountedPricePerQuantity = actualPrice.subtract(equalDiscountPerQuantity);
//
//                BigDecimal discountedPricePerQuantity = actualPrice.subtract(equalDiscountPerQuantity);
//
//// Set discountedPricePerQuantity to zero if it's less than zero
//                discountedPricePerQuantity = discountedPricePerQuantity.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : discountedPricePerQuantity;
//
//                System.out.println(discountedPricePerQuantity);
//
//                // Update the product's discountPrice for each quantity
//                cartItem.setDiscountPrice(discountedPricePerQuantity);
//
//                // Save the updated product (if needed)
//                cartService.saveOrUpdateCartItem(cartItem);
//            }
//
//            // The coupon has been successfully applied
//            return true;
//        }
//
//        // Coupon code does not match any valid coupon
//        return false;
//    }


}

