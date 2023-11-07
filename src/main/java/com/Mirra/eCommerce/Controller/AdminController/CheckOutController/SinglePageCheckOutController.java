package com.Mirra.eCommerce.Controller.AdminController.CheckOutController;


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
import com.Mirra.eCommerce.Service.Orders.OrderService;
import com.Mirra.eCommerce.Service.Product.ProductService;
import com.Mirra.eCommerce.Service.User.AddressService;
import com.Mirra.eCommerce.Service.User.Related.CartlistService;
import com.Mirra.eCommerce.Service.User.Related.WalletService;
import com.Mirra.eCommerce.Service.User.Related.WishlistService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user/checkOut")
public class SinglePageCheckOutController {

    @Autowired
    private UserService userService;


    @Autowired
    private CartlistService cartlistService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private CalculationService calculateActualTotal;

    @Autowired
    private CreateOrderService createOrderService;

    @Autowired
    private CartOrderItemsService cartOrderItemsService;

    @Autowired
    private SelectPaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UpdateStockService updateStockService;

    @Autowired
    private WishlistService wishlistService;


    @Autowired
    private WalletUpadteService walletUpadteService;



    @GetMapping("/single")
    public String checkOut(
            @RequestParam("productId") Long productId,
            @RequestParam("productQuantity") int productQuantity,
            Model model,
            RedirectAttributes ra,
            HttpServletRequest request,
            HttpSession session
    ) {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse == null) {
            return "redirect:/signin";
        }

        String username = jwtResponse.getUsername();
        User user = userService.findByEmail(username);
        Product product = productService.getProductById(productId);


        if (product.getStock() == 0) {
            ra.addFlashAttribute("error", "Product out of stock");
            return "redirect:" + request.getHeader("Referer");
        }
        handleProductQuantity(productQuantity, user, product, model);
        retrieveAndAddAddresses(user, model);


        int totalQuantity = 0;
        int wishListCount = 0;

        int loggedInUserId = user.getId();
        totalQuantity = cartlistService.getCartListCountForUser(loggedInUserId);
        wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("wishListCount", wishListCount);

        return "Admin/Checkout/SingleProductCheckout";
    }

    private void handleProductQuantity(int productQuantity, User user, Product product, Model model) {
        if (productQuantity == 1 && !cartlistService.existsByUserAndProduct(user,product)) {

                cartlistService.addToCartlist(product.getId(), user.getEmail());
                cartlistService.findCart(user, product);
                AddToCart cart = cartlistService.findCart(user, product);

                cart.setQuantity(1);
                cartlistService.updateCart(cart);
                model.addAttribute("cart",cart);
            BigDecimal grandTotal = calculateActualTotal.calculateGrandTotal(cart);
            model.addAttribute("grandTotal", grandTotal);

        } else {
            AddToCart cart = cartlistService.findCart(user, product);
            cart.setQuantity(productQuantity);
            cartlistService.updateCart(cart);
            model.addAttribute("cart",cart);
            BigDecimal grandTotal = calculateActualTotal.calculateGrandTotal(cart);
            model.addAttribute("grandTotal", grandTotal);
        }
    }

    private void retrieveAndAddAddresses(User user, Model model) {
        int loggedInUserId = user.getId();
        List<Address> addresses = addressService.findAddressesByUserId(loggedInUserId);
        model.addAttribute("addresses", addresses);
        model.addAttribute("user", user);
        Wallet wallet = walletService.findByUser(user);
        if (wallet != null) {
            model.addAttribute("walletAmount", wallet.getAmount());
        }

    }


    @Transactional
    @PostMapping("/single")
    public String placeSingleOrder(@ModelAttribute("user") User user,
                                   @RequestParam("cart.id") int cartId,
                                   @RequestParam(value ="grandTotal", required = false) BigDecimal grandTotal,
                                   @RequestParam(value = "selectedAddress", required = false) Integer selectedAddressId,
                                   @RequestParam(value = "paymentMethod", required = false) String paymentMethod,
                                   @RequestParam(value = "walletBalanceAmount", required = false) BigDecimal amount,
                                   Model model, HttpServletRequest request,RedirectAttributes ra) throws IOException, ClassNotFoundException {



        AddToCart cart=cartlistService.findById(cartId);


        // Check if selectedAddressId is null or 0
        if (selectedAddressId == null || selectedAddressId == 0) {
            ra.addFlashAttribute("errorMessage", "Please select a valid address.");
            return "redirect:" + request.getHeader("Referer");
        }
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            ra.addFlashAttribute("errorMessage", "Please select a payment method.");
            return "redirect:" + request.getHeader("Referer");
        }

        if (grandTotal.subtract(amount).compareTo(BigDecimal.ZERO) == 0 && !paymentMethod.equals("WALLET")) {
            ra.addFlashAttribute("errorMessage", "Invalid payment selection.");
            return "redirect:" + request.getHeader("Referer");
        }

        // Fetch the selected address from the database
        Address selectedAddress = addressService.getAddressById(selectedAddressId);
        // Create a new order
        Order order =createOrderService.createOrder(user, selectedAddress, grandTotal,cart.getProducts().getActualPrice());

        cartOrderItemsService.createOrderItems(order, cart);
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
            updateStockService.updateProductStock(cart);
            // Clear the user's cart
            cartlistService.removeCartItem(cartId);
            if(!amount.equals(BigDecimal.ZERO)){
                // Handle wallet logic
                walletUpadteService.handleWallet(order, user, grandTotal, amount);
            }


        int id = order.getId();
        return "redirect:/user/orders/invoice/" + id;

    }








}
