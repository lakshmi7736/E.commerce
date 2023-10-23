package com.Mirra.eCommerce.Controller.AdminController.ServiceImageController;

import com.Mirra.eCommerce.DTO.BannerDto;
import com.Mirra.eCommerce.Models.ServiceImages.Banner;
import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Service.Category.CategoryService;
import com.Mirra.eCommerce.Service.ServiceImages.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
@RequestMapping("/admin")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/addBanner")
    public String showBannerForm(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("newBanner", new Banner()); // Add an empty category for the form
        return "Admin/ServiceImages/bannerForm";
    }

    @PostMapping("/addBanner")
    public String addBanner(@ModelAttribute("newBanner") BannerDto bannerDto,
                            @RequestParam("imageFile") MultipartFile imageFile, Model model) {

        // Set image data from the uploaded file
        try {
            byte[] imageData = imageFile.getBytes();
            bannerDto.setImageData(Base64.getEncoder().encodeToString(imageData));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, e.g., by showing an error message to the user
            model.addAttribute("uploadError", "Failed to upload the image.");
            return "Admin/ServiceImages/bannerForm"; // Replace with the appropriate error view name
        }

        // Call your service to add the banner
        bannerService.addBanner(bannerDto);

        // Redirect to a success page or another appropriate URL
        return "redirect:/admin/addBanner?success"; // Replace with the URL where you want to redirect after a successful submission
    }

}
