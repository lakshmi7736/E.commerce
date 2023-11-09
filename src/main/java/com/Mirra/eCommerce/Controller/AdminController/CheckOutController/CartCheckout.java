package com.Mirra.eCommerce.Controller.AdminController.CheckOutController;

import com.Mirra.eCommerce.Models.Coupons.Coupon;
import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderStatus;
import com.Mirra.eCommerce.Models.Orders.OrderStatusHistory;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Address;
import com.Mirra.eCommerce.Models.Users.Payment;
import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import com.Mirra.eCommerce.Models.Users.Related.Wallet;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Service.Checkout.*;
import com.Mirra.eCommerce.Service.Coupons.CouponService;
import com.Mirra.eCommerce.Service.Orders.OrderService;
import com.Mirra.eCommerce.Service.Product.ProductService;
import com.Mirra.eCommerce.Service.User.AddressService;
import com.Mirra.eCommerce.Service.User.Related.CartlistService;
import com.Mirra.eCommerce.Service.User.Related.WalletService;
import com.Mirra.eCommerce.Service.User.Related.WishlistService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart/checkout")
public class CartCheckout {


    @Autowired
    private UserService userService;

    @Autowired
    private CartlistService cartlistService;

    @Autowired
    private CalculationService calculateActualTotal;

    @Autowired
    private AddressService addressService;

    @Autowired
    private WalletService walletService;


    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productsService;

    @Autowired
    private SelectPaymentService paymentService;

    @Autowired
    private CreateOrderService createOrderService;

    @Autowired
    private CartOrderItemsService cartOrderItemsService;


    @Autowired
    private UpdateStockService updateStockService;


    @Autowired
    private WalletUpadteService walletUpadteService;


    @Autowired
    private CouponService couponService;

    @Autowired
    private CartlistService cartService;




    

    @GetMapping
    public String cart(Model model, HttpSession session) throws IOException, ClassNotFoundException {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse == null) {
            return "redirect:/signin"; // Use a meaningful URL
        }

        String username = jwtResponse.getUsername();
        User user = userService.findByEmail(username);
        model.addAttribute("user",user);

            Wallet wallet = walletService.findByUser(user);
            if (wallet != null) {
                model.addAttribute("walletAmount", wallet.getAmount());
            }

            int loggedInUserId = user.getId();



            List<Address> addresses = addressService.findAddressesByUserId(loggedInUserId);
            model.addAttribute("addresses", addresses);

            List<AddToCart> cartList = cartlistService.getCartListByUserId(loggedInUserId);
            model.addAttribute("cartlist", cartList);

            for (AddToCart cart:cartList){
                if(cart.getDiscountPrice()!=null){
                    BigDecimal appliedCoupon = calculateActualTotal.calculateGrandCouponTotal(cartList);
                    model.addAttribute("appliedCoupon", appliedCoupon);
                }
            }



        int totalQuantity = cartList.size();
            int wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
            model.addAttribute("totalQuantity", totalQuantity);
            model.addAttribute("wishListCount", wishListCount);

            BigDecimal grandTotal = calculateActualTotal.calculateGrandTotal(cartList);
            model.addAttribute("grandTotal", grandTotal);

            BigDecimal subTotal = calculateActualTotal.calculateActualTotal(cartList);
            model.addAttribute("subTotal",subTotal);


        return "Admin/Checkout/CartCheckout";
    }


    @Transactional
    @PostMapping
    public String placeOrder(@ModelAttribute("user") User user,
                             @RequestParam(value = "appliedCoupon", required = false) BigDecimal appliedCoupon,
                             @RequestParam(value = "grandTotal", required = false) BigDecimal grandTotaled, @RequestParam("subTotal") BigDecimal subTotal,
                             @RequestParam(value = "selectedAddress", required = false) Integer selectedAddressId,
                             @RequestParam(value = "paymentMethod", required = false) String paymentMethod,
                             @RequestParam(value = "walletBalanceAmount", required = false) BigDecimal amount,
                             Model model,HttpSession session) throws IOException, ClassNotFoundException {


        BigDecimal grandTotal;

        if (appliedCoupon != null) {
            grandTotal = appliedCoupon;
        } else {
            grandTotal = grandTotaled;
        }

        // Check if selectedAddressId is null or 0
        if (selectedAddressId == null || selectedAddressId == 0) {
            model.addAttribute("errorMessage", "Please select a valid address.");
            return cart(model,session);
        }
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            model.addAttribute("errorMessage", "Please select a payment method.");
            return cart(model,session);
        }

        if(amount.equals(null) && paymentMethod.equals("WALLET")){
            model.addAttribute("errorMessage", "Invalid payment selection.");
            return cart(model,session);
        }

        if (grandTotal.subtract(amount).compareTo(BigDecimal.ZERO) == 0 && !paymentMethod.equals("WALLET")) {
            System.out.println("not wallet");
            model.addAttribute("errorMessage", "Invalid payment selection.");
            return cart(model, session);
        }

        // Fetch the selected address from the database
        Address selectedAddress = addressService.getAddressById(selectedAddressId);
        // Create a new order
        Order order =createOrderService.createOrder(user, selectedAddress, grandTotal,subTotal);

        List<AddToCart> cartList = cartlistService.getCartListByUserId(user.getId());

        cartOrderItemsService.createOrderItems(order, cartList);
        // Convert the selected payment method String to the Payment enum
        Payment selectedPayment = paymentService.selectPaymentMethod(paymentMethod);
        order.setMethod(selectedPayment);

        // Set the order date to the current date and time
        order.setOrderDate(LocalDateTime.now());




        // Create a new status change history entry
        OrderStatusHistory statusHistory = new OrderStatusHistory();
        statusHistory.setOrder(order);
        statusHistory.setNewStatus(OrderStatus.ORDERED);
        statusHistory.setChangeTimestamp(LocalDateTime.now());

        // Update the order's status
        order.setStatus(OrderStatus.ORDERED);

        // Add the status change history entry to the order
        if (order.getStatusHistory() == null) {
            order.setStatusHistory(new ArrayList<>());
        }
        order.getStatusHistory().add(statusHistory);

        // Save the updated order
        orderService.saveOrder(order);


            // Update product stock (assuming you have a method for this)
            updateStockService.updateProductStock(cartList);
            // Clear the user's cart
            cartlistService.clearCartByUser(user);
            if(!amount.equals(BigDecimal.ZERO)){
                // Handle wallet logic
                walletUpadteService.handleWallet(order, user, grandTotal, amount);
            }


        int id = order.getId();
        return "redirect:/user/orders/invoice/" + id;

    }




    @PostMapping("/applyCoupon/cart")
    public String applyCoupon(@RequestParam("couponCode") String couponCode,@RequestParam("grandTotal") BigDecimal grandTotal, HttpSession session, Model model, RedirectAttributes ra) throws IOException, ClassNotFoundException {
        System.out.println("INIDE COUPON POST");
        System.out.println(couponCode);
        System.out.println(grandTotal);

        Coupon coupon = couponService.findByCode(couponCode);
        System.out.println(coupon.getCode());

        // Convert minPurchase (double) to a BigDecimal for comparison
        BigDecimal minPurchase = BigDecimal.valueOf(coupon.getMinPurchaseAmt());

        // Compare BigDecimal using compareTo
        if (grandTotal.compareTo(minPurchase) < 0) {
            ra.addFlashAttribute("coupon", "Can't apply coupon; didn't reach the minimum purchase.");
            return "redirect:/cart/checkout"; // Redirect to the cart page
        }


        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");


        User user = userService.findByEmail(jwtResponse.getUsername());


        boolean couponApplied = applyCouponLogic(couponCode,user.getId()); // Replace with your coupon application logic

        if (couponApplied) {
            System.out.println("CHECKED COUPON APPLIED");
            model.addAttribute("couponApplied", true);
            // Add other coupon-related data to the model if needed
            ra.addFlashAttribute("coupon", "Added Coupon.");
        }

        return "redirect:/cart/checkout"; // Redirect to the cart page

    }

    public boolean applyCouponLogic(String couponCode, int userId) {
        // Assuming you have a list of valid coupon codes and their details
        // You can replace this with your actual coupon validation logic
        Coupon validCoupon = couponService.findByCode(couponCode);

        if (validCoupon != null) {
            // Get the cart items for the user
            List<AddToCart> cartItems = cartService.getCartListByUserId(userId);

            // Calculate the discount amount based on the coupon's rules
            BigDecimal discountAmount = BigDecimal.valueOf(validCoupon.getDiscountAmount());

            // Calculate the total quantity of products in the cart
            int totalQuantity = cartItems.stream()
                    .mapToInt(AddToCart::getQuantity)
                    .sum();

            // Calculate the equal discount amount per product quantity
            BigDecimal equalDiscountPerQuantity = discountAmount.divide(BigDecimal.valueOf(totalQuantity), 2, RoundingMode.HALF_UP);
            System.out.println("equalDiscountPerQuantity "+equalDiscountPerQuantity);

            // Apply the discount to each cart item's product
            for (AddToCart cartItem : cartItems) {
                Product product = cartItem.getProducts();

                // Calculate the discounted price for each quantity
                BigDecimal actualPrice = product.getMyPrice();

//                BigDecimal discountedPricePerQuantity = actualPrice.subtract(equalDiscountPerQuantity);

                BigDecimal discountedPricePerQuantity = actualPrice.subtract(equalDiscountPerQuantity);

// Set discountedPricePerQuantity to zero if it's less than zero
                discountedPricePerQuantity = discountedPricePerQuantity.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : discountedPricePerQuantity;


                // Update the product's discountPrice for each quantity
                cartItem.setDiscountPrice(discountedPricePerQuantity);


                // Save the updated product (if needed)
                cartService.updateCart(cartItem);
            }
            // The coupon has been successfully applied
            return true;
        }

        // Coupon code does not match any valid coupon
        return false;
    }



}
