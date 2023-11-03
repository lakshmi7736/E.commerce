package com.Mirra.eCommerce.Controller.AdminController.CheckOutController;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderStatus;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Address;
import com.Mirra.eCommerce.Models.Users.Payment;
import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import com.Mirra.eCommerce.Models.Users.Related.Wallet;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.Checkout.*;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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


            BigDecimal grandTotal = calculateActualTotal.calculateGrandTotal(cartList);
            model.addAttribute("grandTotal", grandTotal);

            BigDecimal subTotal = calculateActualTotal.calculateActualTotal(cartList);
            model.addAttribute("subTotal",subTotal);


            int totalQuantity = cartlistService.getCartListCountForUser(loggedInUserId);
            model.addAttribute("totalQuantity", totalQuantity);

            int wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
            model.addAttribute("wishListCount", wishListCount);

        return "Admin/Checkout/CartCheckout";
    }


    @Transactional
    @PostMapping
    public String placeOrder(@ModelAttribute("user") User user,
                             @RequestParam("grandTotal") BigDecimal grandTotal, @RequestParam("subTotal") BigDecimal subTotal,
                             @RequestParam(value = "selectedAddress", required = false) Integer selectedAddressId,
                             @RequestParam(value = "paymentMethod", required = false) String paymentMethod,
                             @RequestParam(value = "walletBalanceAmount", required = false) BigDecimal amount,
                             Model model){

        System.out.println("walletBalanceAmount"+amount);;

        // Check if selectedAddressId is null or 0
        if (selectedAddressId == null || selectedAddressId == 0) {
            model.addAttribute("errorMessage", "Please select a valid address.");
            return "Admin/Checkout/CartCheckout";
        }

        if (paymentMethod == null || paymentMethod.isEmpty()) {
            model.addAttribute("errorMessage", "Please select a payment method.");
            return "Admin/Checkout/CartCheckout";
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
        order.setStatus(OrderStatus.ORDERED); // Assuming the initial status is ORDERED

        if (selectedPayment != Payment.UPI) {
            // Save the order to the database
            orderService.saveOrder(order);
            // Update product stock (assuming you have a method for this)
            updateStockService.updateProductStock(cartList);
            // Clear the user's cart
            cartlistService.clearCartByUser(user);
            // Handle wallet logic
            walletUpadteService.handleWallet(order, user, grandTotal, amount);
        }
        model.addAttribute("successMessage", "Order placed successfully!");
        return "redirect:/user/profile";

    }













}