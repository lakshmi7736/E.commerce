package com.Mirra.eCommerce.Controller.AdminController.ServiceImageController;


import com.Mirra.eCommerce.DTO.InstagramDto;
import com.Mirra.eCommerce.Models.ServiceImages.Instagram;
import com.Mirra.eCommerce.Service.ServiceImages.InstagramImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class InstagramController {


    @Autowired
    private InstagramImageService instagramImageService;



    @GetMapping("/instagram")
    public String showBannerForm(Model model) {
        List<Instagram> instagrams=instagramImageService.findAll();
        model.addAttribute("instagrams",instagrams);
        model.addAttribute("newInstagramImage", new Instagram()); // Add an empty category for the form
        return "Admin/ServiceImages/InstagramForm";
    }


    @PostMapping("/addInstagram")
    public String addBanner(@ModelAttribute("newInstagramImage") InstagramDto instagramDto,
                            @RequestParam("imageFile") MultipartFile imageFile, Model model) {

        // Set image data from the uploaded file
        try {
            byte[] imageData = imageFile.getBytes();
            instagramDto.setImageData(Base64.getEncoder().encodeToString(imageData));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, e.g., by showing an error message to the user
            model.addAttribute("uploadError", "Failed to upload the image.");
            return "Admin/ServiceImages/InstagramForm"; // Replace with the appropriate error view name
        }

        // Call your service to add the banner
        instagramImageService.addInstagram(instagramDto);

        // Redirect to a success page or another appropriate URL
        return "redirect:/admin/instagram"; // Replace with the URL where you want to redirect after a successful submission
    }

}
