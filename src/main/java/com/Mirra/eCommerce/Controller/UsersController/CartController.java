package com.Mirra.eCommerce.Controller.UsersController;

import com.Mirra.eCommerce.Models.Coupons.Coupon;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Service.Checkout.CalculationService;
import com.Mirra.eCommerce.Service.Coupons.CouponService;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
import com.Mirra.eCommerce.Service.Orders.OrderAdditionalService;
import com.Mirra.eCommerce.Service.Orders.OrderService;
import com.Mirra.eCommerce.Service.Product.ProductService;
import com.Mirra.eCommerce.Service.User.Related.CartlistService;
import com.Mirra.eCommerce.Service.User.Related.WishlistService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/user/cart")
public class CartController {

    @Autowired
    private UserService userService;



    @Autowired
    private CartlistService cartlistService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CalculationService calculateActualTotal;

    @Autowired
    private SerializeAndDeserialize serializeAndDeserialize;


    @Autowired
    private CouponService couponService;


    @Autowired
    private OrderAdditionalService orderService;



    @GetMapping
    public String cart(Model model, HttpSession session) throws IOException, ClassNotFoundException {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse == null) {
            return "redirect:/signin";
        }

        User user = getUserFromJwtResponse(jwtResponse);

        if (user == null) {
            return "redirect:/signin";
        }

        List<AddToCart> cartList = getCartListForUser(user);



        BigDecimal grandTotal = calculateActualTotal.calculateGrandTotal(cartList);
        model.addAttribute("grandTotal", grandTotal);


        BigDecimal subTotal = calculateActualTotal.calculateActualTotal(cartList);
        model.addAttribute("subTotal", subTotal);

        List<String> encodedImagesList = encodeImages(cartList);
        model.addAttribute("encodedImagesList", encodedImagesList);

        int totalQuantity = cartList.size();
        int wishListCount = wishlistService.getWishListCountForUser(user.getId());
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("wishListCount", wishListCount);

        List<Coupon> nonExpiredCoupons = getNonExpiredCoupons(user, grandTotal);
        model.addAttribute("couponsInRange", nonExpiredCoupons);
        model.addAttribute("cartlist", cartList);

        return "User/Related/myCart";
    }


    
    private User getUserFromJwtResponse(JwtResponse jwtResponse) {
        String username = jwtResponse.getUsername();
        return userService.findByEmail(username);
    }

    private List<AddToCart> getCartListForUser(User user) {

        List<AddToCart> cartList= cartlistService.getCartListByUserId(user.getId());
        for (AddToCart list :cartList){
            list.setDiscountPrice(null);
            cartlistService.updateCart(list);
        }

        return cartList;
    }




    private List<Coupon> getNonExpiredCoupons(User user, BigDecimal grandTotal) {
        LocalDate currentDate = LocalDate.now();
        User mostOrderedUser = orderService.findMostOrderedUser();

        if (mostOrderedUser != null && user.getId() == mostOrderedUser.getId()) {
            return couponService.getCouponsForUser();
        } else {
             BigDecimal subtractedValue = grandTotal.subtract(new BigDecimal(500));
            double subtractedDouble = subtractedValue.doubleValue();

            BigDecimal addedValue = grandTotal.add(new BigDecimal(500));
            double addedDouble = addedValue.doubleValue();
            List<Coupon> couponsInRange = couponService.getCouponsInRange(subtractedDouble,addedDouble);

            return couponsInRange.stream()
                    .filter(coupon -> {
                        LocalDate expirationDate = coupon.getExpirationDate();
                        return expirationDate != null && !expirationDate.isBefore(currentDate) && coupon.getMinPurchaseAmt() != 0;
                    })
                    .collect(Collectors.toList());
        }
    }


//    @GetMapping
//    public String cart(Model model, HttpSession session) throws IOException, ClassNotFoundException {
//        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");
//
//        if (jwtResponse == null) {
//            return "redirect:/signin"; // Use a meaningful URL
//        }
//
//        String username = jwtResponse.getUsername();
//        User user = userService.findByEmail(username);
//
//        if (user != null) {
//            int loggedInUserId = user.getId();
//
//            List<AddToCart> cartList = cartlistService.getCartListByUserId(loggedInUserId);
//
//            BigDecimal grandTotal = calculateActualTotal.calculateGrandTotal(cartList);
//            BigDecimal subTotal = calculateActualTotal.calculateActualTotal(cartList);
//            List<String> encodedImagesList = encodeImages(cartList);
//
//            int totalQuantity = cartlistService.getCartListCountForUser(loggedInUserId);
//            int wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
//            model.addAttribute("totalQuantity", totalQuantity);
//            model.addAttribute("wishListCount", wishListCount);
//
//            model.addAttribute("grandTotal", grandTotal);
//
//            model.addAttribute("subTotal",subTotal);
//
//            model.addAttribute("cartlist", cartList);
//            model.addAttribute("encodedImagesList", encodedImagesList);
//
//            BigDecimal subtractedValue = grandTotal.subtract(new BigDecimal(500));
//            double subtractedDouble = subtractedValue.doubleValue();
//
//            BigDecimal addedValue = grandTotal.add(new BigDecimal(500));
//            double addedDouble = addedValue.doubleValue();
//
//
//
//            // Retrieve coupons within the specified range
//            List<Coupon> couponsInRange = couponService.getCouponsInRange(subtractedDouble, addedDouble);
//
//            for(Coupon c:couponsInRange){
//                System.out.println(c.getCode());
//            }
//            List<Coupon> nonExpiredCoupons = new ArrayList<>(); // Create a new list to store non-expired coupons
//            User mostOrderedUser = orderService.findMostOrderedUser();
//            System.out.println("mostOrderedUser "+mostOrderedUser);
//            System.out.println("user "+user);
//
//            LocalDate currentDate = LocalDate.now();
//
//            if(mostOrderedUser!=null && user!=null && user.getId()== mostOrderedUser.getId()){
//                System.out.println("inside yes");
//                List<Coupon> userCoupon =couponService.getCouponsForUser();
//                nonExpiredCoupons = userCoupon;
//
//            }
//            else {
//                for (Coupon coupon : couponsInRange) {
//                    System.out.println("to fetch coupons");
//                    LocalDate expirationDate = coupon.getExpirationDate();
//                    if (expirationDate != null && !expirationDate.isBefore(currentDate)&& coupon.getMinPurchaseAmt()!=0) {
//                        nonExpiredCoupons.add(coupon); // Add non-expired coupons to the new list
//                    }
//                }
//                model.addAttribute("couponsInRange", nonExpiredCoupons); // Add the new list to the model
//
//            }
//        }
//
//        return "User/Related/myCart";
//    }
//





    private List<String> encodeImages(List<AddToCart> cartList) throws IOException, ClassNotFoundException {
        List<String> encodedImagesList = new ArrayList<>();
        for (AddToCart cartItem : cartList) {
            Product product = cartItem.getProducts();
            List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());
            if (!imageDataList.isEmpty()) {
                String encodedImage = Base64.getEncoder().encodeToString(imageDataList.get(0));
                encodedImagesList.add(encodedImage);
            }
        }
        return encodedImagesList;
    }



    @GetMapping("/add/{productId}")
    public String addToCartist(@PathVariable Long productId, HttpSession session, RedirectAttributes ra, HttpServletRequest request, @RequestParam(defaultValue = "true", required = false) boolean add) {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse == null) {
            return "redirect:/signin";
        }

        String username = jwtResponse.getUsername();
        User user = userService.findByEmail(username);

        if (productId == null) {
            ra.addFlashAttribute("error", "Product is null");
            return "redirect:" + request.getHeader("Referer");
        }

        Product product = productService.getProductById(productId);

        if (cartlistService.existsByUserAndProduct(user, product)) {
            int stock= product.getStock();
            if(stock !=0 && add==true){
               AddToCart cart=cartlistService.findCart(user,product);
               if (cart.getQuantity()==product.getStock()){
                   ra.addFlashAttribute("error", "cart item can't exceed the stock");
               }else {
                   cart.setQuantity(cart.getQuantity()+1);
                   cartlistService.updateCart(cart);
                   ra.addFlashAttribute("success", "Quantity updated");
               }

            }else if(stock !=0 && add==false){
                AddToCart cart=cartlistService.findCart(user,product);
                if(cart.getQuantity()>1){
                    cart.setQuantity(cart.getQuantity()-1);
                    cartlistService.updateCart(cart);
                    ra.addFlashAttribute("success", "Quantity updated");
                }else{
                    ra.addFlashAttribute("error", "Can't decrease quantity.Please do remove product");
                }

            }

            else {
                ra.addFlashAttribute("error", "Product out-of stock");
            }

        } else {
            cartlistService.addToCartlist(productId, username);
            ra.addFlashAttribute("success", "Added to the cart");
        }

        return "redirect:" + request.getHeader("Referer");
    }



    @RequestMapping("removeProduct/{cartlistItem}")
    public String removeProductFromCart(@PathVariable("cartlistItem") Integer cartlistItem) {

        if (cartlistItem != null)
            // Remove the product from the cart
            cartlistService.removeCartItem(cartlistItem);

        return "redirect:/user/cart";
    }


}
