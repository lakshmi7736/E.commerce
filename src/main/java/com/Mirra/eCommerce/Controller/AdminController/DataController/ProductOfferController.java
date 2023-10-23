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


@Controller
@RequestMapping("/admin/product")
public class ProductOfferController {


    @Autowired
    private ProductService productsService;

    @Autowired
    private ProductOfferService productOfferService;

    @GetMapping("/offer/{productId}")
    public String categoryOffer(@PathVariable Long productId, Model model) {
        Product product = productsService.getProductById(productId);
        if (product == null) {
            // Handle product not found case, e.g., redirect to an error page
            return "error";
        }
        model.addAttribute("product", product);
        model.addAttribute("newOffer", new ProductOffer()); // Add an empty category for the form
        return "Admin/dashBoard/products/ProductOffer";
    }


    @PostMapping("/offer/{productId}")
    public String addProductOffer(@PathVariable Long productId,@ModelAttribute("newOffer") ProductOfferDto productOfferDto, Model model) {

        Product product = productsService.getProductById(productId);
        if (product == null) {
            // Handle product not found case, e.g., redirect to an error page
            return "error";
        }

        // Call your service to add the offer
        productOfferService.saveProductOffer(productId,productOfferDto);

        // Redirect to a success page or another appropriate URL
        return "redirect:/admin/product"; // Replace with the URL where you want to redirect after a successful submission
    }


}
