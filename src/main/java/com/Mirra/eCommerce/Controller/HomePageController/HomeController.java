package com.Mirra.eCommerce.Controller.HomePageController;


import com.Mirra.eCommerce.DTO.BannerDto;
import com.Mirra.eCommerce.Models.ServiceImages.Banner;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.CategoryOffer;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Models.datas.ProductReview;
import com.Mirra.eCommerce.Service.Category.CategoryOfferService;
import com.Mirra.eCommerce.Service.Category.CategoryService;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
import com.Mirra.eCommerce.Service.Product.CalculateAverageRatingService;
import com.Mirra.eCommerce.Service.Product.ProductReviewService;
import com.Mirra.eCommerce.Service.Product.ProductService;
import com.Mirra.eCommerce.Service.ServiceImages.BannerService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
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
    private SerializeAndDeserialize serializeAndDeserialize;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private CalculateAverageRatingService calculateAverageRatingService;

    @Autowired
    private CategoryOfferService categoryOfferService;


    @GetMapping
    public String index(Model model, HttpSession session) throws IOException, ClassNotFoundException {
        System.out.println("inisde hoem page");

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);


        Banner existingBanner = bannerService.getExistingBanner();
        model.addAttribute("banner", existingBanner);

        List<CategoryOffer> categoryOffers=categoryOfferService.findAll();
        model.addAttribute("categoryOffers",categoryOffers);


        List<Product> products = productsService.getAllProducts();
        Collections.reverse(products);

        List<Product> activeProducts = products.stream()
                .filter(product -> product.isActive())
                .collect(Collectors.toList());

        model.addAttribute("products", activeProducts);


        List<String> encodedImagesList = new ArrayList<>();
        for (Product product : activeProducts) {
            List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());
            String encodedImage = Base64.getEncoder().encodeToString(imageDataList.get(0));
            encodedImagesList.add(encodedImage);

            // Calculate the average rating for each product and add it to the product object
            List<ProductReview> reviews = productReviewService.getReviewsByProductId(product.getId());
            double averageRating = calculateAverageRatingService.calculateAverageRating(reviews);
            product.setAverageRating(averageRating);
        }

        // Retrieve the wishlist count for the logged-in user
        int wishListCount = 0;
        int totalQuantity = 0; // Initialize totalQuantity here

        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");
        if (jwtResponse != null) {
            String username = jwtResponse.getUsername();
            User user = userService.findByEmail(username);
            if (user != null) {
                int loggedInUserId = user.getId();



//                // Retrieve the user's cart items from the database
//                List<AddToCart> cartList = cartService.getCartItemsByUserId(loggedInUserId);
//
//                // Calculate the total quantity of products added to the cart
//                totalQuantity = cartList.stream()
//                        .mapToInt(AddToCart::getQuantity)
//                        .sum();
//
//                // Get the count of wishlist items for the logged-in user
//                wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
            }
        }


        // Add the count of Cart and Wishlist
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("wishListCount", wishListCount);

        model.addAttribute("encodedImagesList", encodedImagesList);

        return "basicTemplates/index";
    }



//    private double calculateAverageRating(List<ProductReview> reviews) {
//        if (reviews.isEmpty()) {
//            return 0.0;
//        }
//
//        int totalRating = 0;
//        for (ProductReview review : reviews) {
//            totalRating += review.getRating();
//        }
//
//        return (double) totalRating / reviews.size();
//    }


    @GetMapping("/register")
    public String register(Model model){
        User user= new User();
        model.addAttribute("user",user);
        return "register";
    }

    @GetMapping("/signin")
    public String login(HttpSession session) {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");
        if(jwtResponse==null){
            return "login";
        }
        else {
            return "redirect:/";
        }
    }



}
