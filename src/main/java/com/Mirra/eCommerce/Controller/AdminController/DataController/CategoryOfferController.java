package com.Mirra.eCommerce.Controller.AdminController.DataController;

import com.Mirra.eCommerce.DTO.BannerDto;
import com.Mirra.eCommerce.DTO.CategoryOfferDto;
import com.Mirra.eCommerce.Models.datas.CategoryOffer;
import com.Mirra.eCommerce.Service.Category.CategoryOfferService;
import com.Mirra.eCommerce.Service.Category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;


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

        // Call your service to add the offer
        categoryOfferService.saveCategoryOffer(categoryOfferDto);

        // Redirect to a success page or another appropriate URL
        return "redirect:/admin/categories"; // Replace with the URL where you want to redirect after a successful submission
    }

}
