package com.Mirra.eCommerce.Controller.HomePageController;

import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Models.datas.ProductReview;
import com.Mirra.eCommerce.Models.datas.SubCategory;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
import com.Mirra.eCommerce.Service.Product.CalculateAverageRatingService;
import com.Mirra.eCommerce.Service.Product.ProductReviewService;
import com.Mirra.eCommerce.Service.Product.ProductService;
import com.Mirra.eCommerce.Service.Product.ProductsAdditionalService;
import com.Mirra.eCommerce.Service.SubCategory.SubCategoryService;
import com.Mirra.eCommerce.Service.User.Related.CartlistService;
import com.Mirra.eCommerce.Service.User.Related.WishlistService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Controller
@RequestMapping("/productView")
public class ProductViewController {

    @Autowired
    private ProductService productsService;

    @Autowired
    private SerializeAndDeserialize serializeAndDeserialize;

    @Autowired
    private UserService userService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private CartlistService cartlistService;

    @Autowired
    private CalculateAverageRatingService calculateAverageRatingService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private ProductsAdditionalService productsAdditionalService;


    @GetMapping("/{productId}")
    public String viewProduct(@PathVariable Long productId, Model model, HttpSession session) throws IOException, ClassNotFoundException {
        Product product = productsService.getProductById(productId);

        if (product == null) {
            // Handle product not found case, e.g., redirect to an error page
            return "error";
        }
        int totalQuantity = 0;
        int wishListCount = 0;
        int productQuantity=0;

        if(product.getStock()!=0){
            productQuantity=1;
        }
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse != null) {
            String username = jwtResponse.getUsername();
            User user = userService.findByEmail(username);

            if (user != null) {
                int loggedInUserId = user.getId();
                totalQuantity = cartlistService.getCartListCountForUser(loggedInUserId);
                wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
                AddToCart cart=cartlistService.findCart(user,product);
                if(cart!=null){
                     productQuantity=cart.getQuantity();
                }
                boolean productInList=wishlistService.existsByUserAndProduct(user,product);
                model.addAttribute("productInList", productInList);

//                boolean productInCartList=cartlistService.existsByUserAndProduct(user,product);
//                model.addAttribute("productInCartList", productInCartList);


            }

        }

        model.addAttribute("productQuantity", productQuantity);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("wishListCount", wishListCount);

        List<String> encodedImages = encodeProductImages(product);

        SubCategory subCategory = product.getSubCategory();
        List<Product> relatedProducts = productsService.getProductsBySubCategoryId(subCategory.getId());
        List<String> encodedRelatedImages = productsAdditionalService.getEncodedImagesList(relatedProducts);

        setProductAttributes(model, product, encodedImages, relatedProducts, encodedRelatedImages);

        return "Products/ProductPage";
    }

    private List<String> encodeProductImages(Product product) throws IOException, ClassNotFoundException {
        List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());
        List<String> encodedImages = new ArrayList<>();

        for (byte[] imageData : imageDataList) {
            String encodedImage = Base64.getEncoder().encodeToString(imageData);
            encodedImages.add(encodedImage);
        }

        return encodedImages;
    }


    private void setProductAttributes(Model model, Product product, List<String> encodedImages, List<Product> relatedProducts, List<String> encodedRelatedImages) {
        model.addAttribute("product", product);
        model.addAttribute("encodedImages", encodedImages);
        model.addAttribute("relatedProducts", relatedProducts);
        model.addAttribute("encodedRelatedImagesLists", encodedRelatedImages);

        ProductReview review = new ProductReview();
        model.addAttribute("review", review);

        List<ProductReview> reviews = productReviewService.getReviewsByProductId(product.getId());
        double averageRating = calculateAverageRatingService.calculateAverageRating(reviews);

        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviewCount", reviews.size());
    }



}
