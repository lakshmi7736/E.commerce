package com.Mirra.eCommerce.Controller.HomePageController;

import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Models.datas.ProductReview;
import com.Mirra.eCommerce.Models.datas.SubCategory;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
import com.Mirra.eCommerce.Service.Product.CalculateAverageRatingService;
import com.Mirra.eCommerce.Service.Product.ProductReviewService;
import com.Mirra.eCommerce.Service.Product.ProductService;
import com.Mirra.eCommerce.Service.SubCategory.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
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
    private CalculateAverageRatingService calculateAverageRatingService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private ProductReviewService productReviewService;


    @GetMapping("/{productId}")
    public String viewProduct(@PathVariable Long productId, Model model) throws IOException, ClassNotFoundException {
        Product product = productsService.getProductById(productId);
        if (product == null) {
            // Handle product not found case, e.g., redirect to an error page
            return "error";
        }

        // Deserialize the image data from the product's imageBlob attribute
        List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());

        // Encode the image data as Base64 strings
        List<String> encodedImages = new ArrayList<>();
        for (byte[] imageData : imageDataList) {
            String encodedImage = Base64.getEncoder().encodeToString(imageData);
            encodedImages.add(encodedImage);
        }
        SubCategory subCategory=product.getSubCategory();

        List<Product> relatedProducts=productsService.getProductsBySubCategoryId(subCategory.getId());
        model.addAttribute("relatedProducts",relatedProducts);

        List<String> encodedRelatedImagesLists = new ArrayList<>();
        for (Product relatedImages : relatedProducts) {
            List<byte[]> imagesRealtedDataList = serializeAndDeserialize.deserializeImageBlob(relatedImages.getImageBlob());
            String encodedImage = Base64.getEncoder().encodeToString(imagesRealtedDataList.get(0));
            encodedRelatedImagesLists.add(encodedImage);

            // Calculate the average rating for each product and add it to the product object
            List<ProductReview> reviews = productReviewService.getReviewsByProductId(product.getId());
            double averageRating = calculateAverageRatingService.calculateAverageRating(reviews);
            product.setAverageRating(averageRating);
        }
        model.addAttribute("encodedRelatedImagesLists", encodedRelatedImagesLists);

        // Create an empty ProductReview object to use for the review form
        ProductReview review = new ProductReview();
        model.addAttribute("review", review);

        // Retrieve the product reviews associated with the product
        List<ProductReview> reviews = productReviewService.getReviewsByProductId(productId);
        model.addAttribute("reviews", reviews);

        // Calculate the average rating for the product
        double averageRating =calculateAverageRatingService.calculateAverageRating(reviews);
        model.addAttribute("averageRating", averageRating);

        // Calculate the number of reviews for the product
        int reviewCount = reviews.size();
        model.addAttribute("reviewCount", reviewCount);

        model.addAttribute("product", product);
        model.addAttribute("encodedImages", encodedImages);

        return "Products/ProductPage"; // Return the name of the view template for product details
    }

}
