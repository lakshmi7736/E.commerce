package com.Mirra.eCommerce.Controller.UsersController;

import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Related.WishList;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/user/wishlist")
public class WishListController {

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

    @GetMapping("/add/{productId}")
    public String addToWishlist(@PathVariable Long productId, HttpSession session, RedirectAttributes ra, HttpServletRequest request) {
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

        if (wishlistService.existsByUserAndProduct(user, product)) {
            ra.addFlashAttribute("error", "Product Already exists");
        } else {
            wishlistService.addToWishlist(productId, username);
            ra.addFlashAttribute("success", "Added to the wishlist");
        }

        return "redirect:" + request.getHeader("Referer");
    }


    @GetMapping
    public String wishList(Model model, HttpSession session) throws IOException, ClassNotFoundException {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse == null) {
            return "redirect:/sigin";

        }
            String username = jwtResponse.getUsername();
            User user = userService.findByEmail(username);

            if (user != null) {
                int loggedInUserId = user.getId();

                // Retrieve the user's wishlist from the database
                List<WishList> wishList = wishlistService.getWishListByUserId(loggedInUserId);


                // Create a list to store encoded images
                List<String> encodedImagesList = new ArrayList<>();

                for (WishList list : wishList) {
                    Product product = list.getProducts(); // Get the associated product
                    List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());

                    // Loop through the images associated with the product
                    for (byte[] imageData : imageDataList) {
                        // Encode each image data to a Base64 string and add to the list
                        String encodedImage = Base64.getEncoder().encodeToString(imageData);
                        encodedImagesList.add(encodedImage);
                    }
                }

                // Get the count of wishlist items and cart items for the logged-in user
                int totalQuantity = cartlistService.getCartListCountForUser(loggedInUserId);
                int wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
                model.addAttribute("totalQuantity", totalQuantity);
                model.addAttribute("wishListCount", wishListCount);

                // Add the wishlist and encoded images to the model
                model.addAttribute("wishlist", wishList);
                model.addAttribute("encodedImagesList", encodedImagesList);
            }


        return "User/Related/Wishlist";


    }


    @RequestMapping("removeProduct/{wishlistId}")
    public String removeProductFromCart(@PathVariable("wishlistId") Integer wishlistId) {

        if (wishlistId != null)
            // Remove the product from the cart
            wishlistService.removeCartItem(wishlistId);

        return "redirect:/user/wishlist";
    }

}
