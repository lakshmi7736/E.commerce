package com.Mirra.eCommerce.Controller.AdminController.DataController;

import com.Mirra.eCommerce.DTO.BannerDto;
import com.Mirra.eCommerce.DTO.CategoryOfferDto;
import com.Mirra.eCommerce.Exception.OfferNotFound;
import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.CategoryOffer;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Service.Category.CategoryOfferService;
import com.Mirra.eCommerce.Service.Category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;


@Controller
@RequestMapping("/admin/categories")
public class CategoryOfferController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryOfferService categoryOfferService;


    @GetMapping("/offer")
    public String categoryOffer(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("newOffer", new CategoryOffer()); // Add an empty category for the form
        return "Admin/dashBoard/categories/CategoryOffer";
    }


    @PostMapping("/offer")
    public String addCategoryOffer(@ModelAttribute("newOffer") CategoryOfferDto categoryOfferDto,
                            @RequestParam("imageFile") MultipartFile imageFile, Model model) {

        // Set image data from the uploaded file
        try {
            byte[] imageData = imageFile.getBytes();
            categoryOfferDto.setImageData(Base64.getEncoder().encodeToString(imageData));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, e.g., by showing an error message to the user
            model.addAttribute("uploadError", "Failed to upload the image.");
            return "Admin/dashBoard/categories/CategoryOffer"; // Replace with the appropriate error view name
        }

        // Check if discountPrice is less than 100
        if (categoryOfferDto.getDiscountPrice().compareTo(new BigDecimal(100)) >= 0) {
            // Add an error message to the model to inform the user
            model.addAttribute("uploadError", "Discount price should be less than 100.");
            return "Admin/dashBoard/categories/CategoryOffer"; // Return to the form with an error message
        }

        // Call your service to add the offer
        categoryOfferService.saveCategoryOffer(categoryOfferDto);

        // Redirect to a success page or another appropriate URL
        return "redirect:/admin/categories"; // Replace with the URL where you want to redirect after a successful submission
    }


    @GetMapping("/offer/{categoryId}")
    public String categoryOfferId(@PathVariable Long categoryId,Model model) {


        Category category = categoryService.findById(categoryId);
        CategoryOffer offers=categoryOfferService.findByCategoryId(categoryId);
        model.addAttribute("offers",offers);

        if (category == null) {
            // Handle product not found case, e.g., redirect to an error page
            return "error";
        }
        model.addAttribute("category", category);
        model.addAttribute("newOffer", new CategoryOffer()); // Add an empty category for the form
        return "Admin/dashBoard/categories/CategoryOffer";
    }


    @PostMapping("/offer/{categoryId}")
    public String addCategoryOfferId(@PathVariable Long categoryId,@ModelAttribute("newOffer") CategoryOfferDto categoryOfferDto,
                                   @RequestParam("imageFile") MultipartFile imageFile, Model model) {

        // Set image data from the uploaded file
        try {
            byte[] imageData = imageFile.getBytes();
            categoryOfferDto.setImageData(Base64.getEncoder().encodeToString(imageData));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, e.g., by showing an error message to the user
            model.addAttribute("uploadError", "Failed to upload the image.");
            return "Admin/dashBoard/categories/CategoryOffer"; // Replace with the appropriate error view name
        }

        // Check if discountPrice is less than 100
        if (categoryOfferDto.getDiscountPrice().compareTo(new BigDecimal(100)) >= 0) {
            // Add an error message to the model to inform the user
            model.addAttribute("uploadError", "Discount price should be less than 100.");
            return "Admin/dashBoard/categories/CategoryOffer"; // Return to the form with an error message
        }

        // Call your service to add the offer
        categoryOfferService.saveCategoryOffer(categoryId,categoryOfferDto);

        // Redirect to a success page or another appropriate URL
        return "redirect:/admin/categories"; // Replace with the URL where you want to redirect after a successful submission
    }


//    delete offer
    @GetMapping("/deleteOffer/{categoryOfferId}")
    public String deleteUser(@PathVariable("categoryOfferId") Integer id, RedirectAttributes ra) {
        CategoryOffer category=categoryOfferService.findById(id) ;
        String name= category.getSaleEvent();
        try {
            categoryOfferService.deleteByCategoryOfferId(id);
            ra.addFlashAttribute("message", "The category offer " + name + " has been deleted.");
        }catch (OfferNotFound e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/categories";
    }


}
