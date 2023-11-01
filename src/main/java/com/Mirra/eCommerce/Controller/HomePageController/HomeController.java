package com.Mirra.eCommerce.Controller.HomePageController;


import com.Mirra.eCommerce.DTO.BannerDto;
import com.Mirra.eCommerce.Models.ServiceImages.Banner;
import com.Mirra.eCommerce.Models.ServiceImages.Instagram;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.*;
import com.Mirra.eCommerce.Service.Category.CategoryOfferService;
import com.Mirra.eCommerce.Service.Category.CategoryService;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
import com.Mirra.eCommerce.Service.Product.*;
import com.Mirra.eCommerce.Service.ServiceImages.BannerService;
import com.Mirra.eCommerce.Service.ServiceImages.InstagramImageService;
import com.Mirra.eCommerce.Service.User.Related.CartlistService;
import com.Mirra.eCommerce.Service.User.Related.WishlistService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productsService;

    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private InstagramImageService instagramImageService;


    @Autowired
    private SerializeAndDeserialize serializeAndDeserialize;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private CartlistService cartlistService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private CalculateAverageRatingService calculateAverageRatingService;

    @Autowired
    private CategoryOfferService categoryOfferService;

    @Autowired
    private ProductOfferService productOfferService;

    @Autowired
    private ProductsAdditionalService productsAdditionalService;

    @GetMapping
    public String index(Model model, HttpSession session) throws IOException, ClassNotFoundException {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        Banner existingBanner = bannerService.getExistingBanner();
        model.addAttribute("banner", existingBanner);

        List<CategoryOffer> categoryOffers = categoryOfferService.findAll();
        model.addAttribute("categoryOffers", categoryOffers);

        List<Product> activeProducts = getActiveProducts(); // Define this method
        model.addAttribute("products", activeProducts);

        List<Instagram> instagrams = instagramImageService.findAll();
        model.addAttribute("instagrams", instagrams);

        List<String> encodedImagesList = productsAdditionalService.getEncodedImagesList(activeProducts);
        model.addAttribute("encodedImagesList", encodedImagesList);

        int totalQuantity = 0;
        int wishListCount = 0;
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse != null) {
            String username = jwtResponse.getUsername();
            User user = userService.findByEmail(username);

            if (user != null) {
                int loggedInUserId = user.getId();
                totalQuantity = cartlistService.getCartListCountForUser(loggedInUserId);
                wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
            }
        }

        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("wishListCount", wishListCount);

        return "basicTemplates/index";
    }

    private List<Product> getActiveProducts() {
        List<Product> products = productsService.getAllProducts();
        Collections.reverse(products);

        return products.stream()
                .filter(Product::isActive)
                .collect(Collectors.toList());
    }




    @GetMapping("/register")
    public String register(Model model){
        User user= new User();
        model.addAttribute("user",user);
        return "register";
    }

    @GetMapping("/signin")
    public String login(HttpSession session) {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse == null) {
            return "login";
        }
        else {
            return "redirect:/";
        }
    }



}
