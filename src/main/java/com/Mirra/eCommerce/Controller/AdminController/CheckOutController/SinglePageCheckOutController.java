package com.Mirra.eCommerce.Controller.AdminController.CheckOutController;

import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Address;
import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Service.Product.ProductService;
import com.Mirra.eCommerce.Service.User.AddressService;
import com.Mirra.eCommerce.Service.User.Related.CartlistService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        return "Admin/Checkout/SingleProductCheckout";
    }

    private void handleProductQuantity(int productQuantity, User user, Product product, Model model) {
        if (productQuantity == 1) {
            if (!cartlistService.existsByUserAndProduct(user, product)) {
                cartlistService.addToCartlist(product.getId(), user.getEmail());
                cartlistService.findCart(user, product);
                AddToCart cart = cartlistService.findCart(user, product);

                cart.setQuantity(1);
                cartlistService.updateCart(cart);
                model.addAttribute("cart",cart);
            }
        } else {
            AddToCart cart = cartlistService.findCart(user, product);
            cart.setQuantity(productQuantity);
            cartlistService.updateCart(cart);
            model.addAttribute("cart",cart);
        }
    }

    private void retrieveAndAddAddresses(User user, Model model) {
        int loggedInUserId = user.getId();
        List<Address> addresses = addressService.findAddressesByUserId(loggedInUserId);
        model.addAttribute("addresses", addresses);
        model.addAttribute("user", user);
    }



//    @GetMapping("/single")
//    public String checkOut(@RequestParam("productId") Long productId, @RequestParam("productQuantity") int productQuantity, Model model, RedirectAttributes ra, HttpServletRequest request, HttpSession session) {
//        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");
//
//        if (jwtResponse == null) {
//            return "redirect:/signin";
//
//        }
//        String username = jwtResponse.getUsername();
//        User user = userService.findByEmail(username);
//        Product product=productService.getProductById(productId);
//        if(product.getStock()==0){
//            ra.addFlashAttribute("error", "Product out of stock");
//            return "redirect:" + request.getHeader("Referer");
//
//        }
//        if(productQuantity==1){
//            if(!cartlistService.existsByUserAndProduct(user,product)){
//                cartlistService.addToCartlist(productId, username);
//                AddToCart cart=cartlistService.findCart(user,product);
//                cart.setQuantity(1);
//                cartlistService.updateCart(cart);
//                model.addAttribute("cart",cart);
//            } else {
//                AddToCart cart=cartlistService.findCart(user,product);
//                model.addAttribute("cart",cart);
//            }
//        }else {
//            AddToCart cart=cartlistService.findCart(user,product);
//            cart.setQuantity(productQuantity);
//            cartlistService.updateCart(cart);
//            model.addAttribute("cart",cart);
//        }
//
//            int loggedInUserId = user.getId();
//            // Retrieve the user's addresses using the user's id
//            List<Address> addresses = addressService.findAddressesByUserId(loggedInUserId);
//            model.addAttribute("addresses", addresses);
//            model.addAttribute("user",user);
//
//        return "Admin/Checkout/SingleProductCheckout";
//    }



    

}