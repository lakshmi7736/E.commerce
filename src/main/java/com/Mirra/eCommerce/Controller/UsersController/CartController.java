package com.Mirra.eCommerce.Controller.UsersController;

import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


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
    private SerializeAndDeserialize serializeAndDeserialize;
    @GetMapping
    public String cart(Model model, HttpSession session) throws IOException, ClassNotFoundException {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse == null) {
            return "redirect:/signin"; // Use a meaningful URL
        }

        String username = jwtResponse.getUsername();
        User user = userService.findByEmail(username);

        if (user != null) {
            int loggedInUserId = user.getId();

            List<AddToCart> cartList = cartlistService.getCartListByUserId(loggedInUserId);

            double grandTotal = calculateGrandTotal(cartList);
            List<String> encodedImagesList = encodeImages(cartList);

            int totalQuantity = cartlistService.getCartListCountForUser(loggedInUserId);
            int wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
            model.addAttribute("totalQuantity", totalQuantity);
            model.addAttribute("wishListCount", wishListCount);

            model.addAttribute("grandTotal", grandTotal);

            model.addAttribute("cartlist", cartList);
            model.addAttribute("encodedImagesList", encodedImagesList);
        }

        return "User/Related/myCart";
    }

    private double calculateGrandTotal(List<AddToCart> cartList) {
        double grandTotal = 0.0;
        for (AddToCart cartItem : cartList) {
            BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
            BigDecimal actualPrice = cartItem.getProducts().getActualPrice();
            BigDecimal total = actualPrice.multiply(quantity);
            cartItem.setTotal(total.doubleValue());
            grandTotal += total.doubleValue();
        }
        return grandTotal;
    }

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
               cart.setQuantity(cart.getQuantity()+1);
               cartlistService.updateCart(cart);
                ra.addFlashAttribute("success", "Quantity updated");
            }else if(stock !=0 && add==false){
                AddToCart cart=cartlistService.findCart(user,product);
                cart.setQuantity(cart.getQuantity()-1);
                cartlistService.updateCart(cart);
                ra.addFlashAttribute("success", "Quantity updated");
            }
            else if(stock ==1 && add==false){
                ra.addFlashAttribute("error", "Can't decrease quantity.Please do remove");
            }
            else {
                ra.addFlashAttribute("error", "Product out-of stock");
            }

        } else {
            cartlistService.addToCartlist(productId, username);
            ra.addFlashAttribute("success", "Added to the wishlist");
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
