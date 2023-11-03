package com.Mirra.eCommerce.Controller.AdminController.CheckOutController;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Orders.OrderStatus;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Address;
import com.Mirra.eCommerce.Models.Users.Payment;
import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import com.Mirra.eCommerce.Models.Users.Related.Wallet;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Service.Calculations.CalculationService;
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
                             Model model){

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
        Order order = createOrder(user, selectedAddress, grandTotal,subTotal);

        List<AddToCart> cartList = cartlistService.getCartListByUserId(user.getId());

        createOrderItems(order, cartList);
        // Convert the selected payment method String to the Payment enum
        Payment selectedPayment = convertPaymentMethod(paymentMethod);
        order.setMethod(selectedPayment);

        // Set the order date to the current date and time
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDERED); // Assuming the initial status is ORDERED

        if (selectedPayment != Payment.UPI) {
            // Save the order to the database
            orderService.saveOrder(order);
            // Update product stock (assuming you have a method for this)
            updateProductStock(cartList);
            // Clear the user's cart
            cartlistService.clearCartByUser(user);
        }
        model.addAttribute("successMessage", "Order placed successfully!");
        return "redirect:/user/profile";

    }


    private Order createOrder(User user, Address selectedAddress, BigDecimal grandTotal,BigDecimal subTotal) {
        Order order = new Order();
        order.setUser(user);
        order.setAddress(selectedAddress);
        order.setGranTotal(subTotal);
        order.setPurchaseTotal(grandTotal);
        return order;
    }

    private void createOrderItems(Order order, List<AddToCart> cartItems) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (AddToCart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProducts());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProducts().getMyPrice());
            orderItem.setActualPrice(cartItem.getProducts().getActualPrice());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
    }


    private Payment convertPaymentMethod(String paymentMethod) {
        switch (paymentMethod) {
            case "COD":
                return Payment.COD;
            case "UPI":
                return Payment.UPI;
            case "WALLET":
                return Payment.WALLET;
            // Handle other payment methods or invalid input if needed
            default:
                throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }
    }



    private void updateProductStock(List<AddToCart> cartItems) {
        for (AddToCart cartItem : cartItems) {
            Product product = cartItem.getProducts();
            int quantityInCart = cartItem.getQuantity();
            int updatedStock = product.getStock() - quantityInCart;

            if (updatedStock > 0) {
                // Only update the stock if it won't go negative
                product.setStock(updatedStock);
                productsService.saveProduct(product); // Save the updated product
            } else {
                product.setActive(false);
                // Only update the stock if it won't go negative
                product.setStock(updatedStock);;
                productsService.saveProduct(product); // Save the updated product

            }
        }
    }

}
