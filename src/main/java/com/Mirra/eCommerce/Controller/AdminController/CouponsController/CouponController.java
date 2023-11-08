//package com.Mirra.eCommerce.Controller.AdminController.CouponsController;
//
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.List;
//
//@Controller
//@RequestMapping("/coupons")
//public class CouponController {
//
//    @Autowired
//    private CouponService couponService;
//
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private CartListService cartService;
//
//    @GetMapping
//    public String showCoupons(Model model) {
//        // Retrieve a list of coupons from the database
//        List<Coupon> coupons = couponService.getAllCoupons();
//        model.addAttribute("coupons", coupons);
//        return "coupon-list";
//    }
//
//    @GetMapping("/create")
//    public String showCreateCouponForm(Model model) {
//        // Display a form for creating a new coupon
//        model.addAttribute("coupon", new Coupon());
//        return "create-coupon";
//    }
//
//    @PostMapping("/create")
//    public String createCoupon(@ModelAttribute Coupon coupon, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "create-coupon"; // Return to the registration form with validation errors.
//        }
//
//        // Check if the coupon code already exists
//        if (couponService.isCouponCodeExists(coupon.getCode())) {
//            bindingResult.rejectValue("code", "duplicate.coupon.code", "Coupon code already exists");
//            return "create-coupon";
//        }
//        else if (coupon.getMinPurchaseAmt()<500 ) {
//            bindingResult.rejectValue("minPurchaseAmt", "duplicate.coupon.minPurchaseAmt", "Minimum purchase amount is 1200");
//            return "create-coupon";
//        }
//
//        else if (coupon.getDiscountAmount()>5000) {
//            bindingResult.rejectValue("discountAmount", "duplicate.coupon.discountAmount", "Discount amount should not exceeds than 5000");
//            return "create-coupon";
//        }
//        else if (coupon.getExpirationDate()==null) {
//            bindingResult.rejectValue("expirationDate", "duplicate.coupon.expirationDate", "Expiry Date required");
//            return "create-coupon";
//        }
//
//        // Create a new coupon and save it to the database
//        couponService.createCoupon(coupon);
//        return "redirect:/coupons";
//    }
//
//
//    @PostMapping("/applyCoupon")
//    public String applyCoupon(@RequestParam("couponCode") String couponCode, @RequestParam("grandTotal") BigDecimal grandTotal, HttpSession session, Model model, RedirectAttributes ra) {
//        System.out.println("INIDE COUPON POST");
//        System.out.println(grandTotal);
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
//        boolean couponApplied = applyCouponLogic(couponCode,user.getId()); // Replace with your coupon application logic
//
//        if (couponApplied) {
//            System.out.println("CHECKED COUPON APPLIED");
//            model.addAttribute("couponApplied", true);
//            // Add other coupon-related data to the model if needed
//            ra.addFlashAttribute("coupon", "Added Coupon.");
//        }
//
//        // Redirect back to the cart page or wherever you want
//        return "redirect:/checkOut"; // Redirect to the cart page
//    }
//
//    public boolean applyCouponLogic(String couponCode, int userId) {
//        // Assuming you have a list of valid coupon codes and their details
//        // You can replace this with your actual coupon validation logic
//        Coupon validCoupon = couponService.findByCode(couponCode);
//
//        if (validCoupon != null) {
//            // Get the cart items for the user
//            List<AddToCart> cartItems = cartService.getCartItemsByUserId(userId);
//
//            // Calculate the discount amount based on the coupon's rules
//            BigDecimal discountAmount = BigDecimal.valueOf(validCoupon.getDiscountAmount());
//
//            // Calculate the total quantity of products in the cart
//            int totalQuantity = cartItems.stream()
//                    .mapToInt(AddToCart::getQuantity)
//                    .sum();
//
//            // Calculate the equal discount amount per product quantity
//            BigDecimal equalDiscountPerQuantity = discountAmount.divide(BigDecimal.valueOf(totalQuantity), 2, RoundingMode.HALF_UP);
//
//            // Apply the discount to each cart item's product
//            for (AddToCart cartItem : cartItems) {
//                Products product = cartItem.getProducts();
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
//
//
//}
//
