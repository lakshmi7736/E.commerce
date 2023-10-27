package com.Mirra.eCommerce.Controller.AdminController.DataController;

import com.Mirra.eCommerce.DTO.CategoryOfferDto;
import com.Mirra.eCommerce.DTO.ProductOfferDto;
import com.Mirra.eCommerce.DTO.ProductUpdateRequest;
import com.Mirra.eCommerce.Models.datas.CategoryOffer;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Models.datas.ProductOffer;
import com.Mirra.eCommerce.Models.datas.SubCategory;
import com.Mirra.eCommerce.Service.Product.ProductOfferService;
import com.Mirra.eCommerce.Service.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;


@Controller
@RequestMapping("/admin/product")
public class ProductOfferController {


    @Autowired
    private ProductService productsService;

    @Autowired
    private ProductOfferService productOfferService;

//    get offer form

    @GetMapping("/offer/{productId}")
    public String categoryOffer(@PathVariable Long productId, Model model) {
        Product product = productsService.getProductById(productId);
        if (product == null) {
            // Handle product not found case, e.g., redirect to an error page
            return "error";
        }
        model.addAttribute("product", product);
        model.addAttribute("newOffer", new ProductOffer()); // Add an empty category for the form

        ProductOffer products=productOfferService.findByProductId(productId);
        model.addAttribute("products",products);
        return "Admin/dashBoard/products/ProductOffer";
    }


//    add offer

    @PostMapping("/offer/{productId}")
    public String addProductOffer(@PathVariable Long productId,@ModelAttribute("newOffer") ProductOfferDto productOfferDto, Model model) {

        Product product = productsService.getProductById(productId);
        if (product == null) {
            // Handle product not found case, e.g., redirect to an error page
            return "error";
        }

        calculateDiscountedPrice(product, productOfferDto.getDiscountPrice());
        // Call your service to add the offer
        productOfferService.saveProductOffer(productId,productOfferDto);

        // Redirect to a success page or another appropriate URL
        return "redirect:/admin/product"; // Replace with the URL where you want to redirect after a successful submission
    }


//    delete offer

    @GetMapping("/deleteOffer/{productOfferId}")
    public String deleteProduct(@PathVariable("productOfferId")  int id, RedirectAttributes ra) {
        System.out.println("inside controler");
        ProductOffer product = productOfferService.findById(id);
        if (product == null) {
            // Handle product not found case, e.g., redirect to an error page
            return "error";
        }
        ProductOffer productInOffer=productOfferService.findById(id);
        Product offerdProduct=productsService.getProductById(productInOffer.getProduct().getId());
        offerdProduct.setMyPrice(BigDecimal.ZERO);
        productsService.saveProduct(offerdProduct);
        // Delete the product from the database
        productOfferService.deleteProductOfferById(id);
        ra.addFlashAttribute("message", "The  " + product.getProduct().getName()+ "'s offer has been deleted");

        return "redirect:/admin/product/productList";
    }



    // Helper methods
    private void calculateDiscountedPrice(Product product, BigDecimal discountPercentage) {
        BigDecimal discountAmount = product.getActualPrice().multiply(discountPercentage.divide(BigDecimal.valueOf(100)));
        BigDecimal discountedPrice = product.getActualPrice().subtract(discountAmount);
        product.setMyPrice(discountedPrice);
        productsService.saveProduct(product);
    }

}
