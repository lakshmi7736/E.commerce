package com.Mirra.eCommerce.Controller.AdminController.DataController;

import com.Mirra.eCommerce.DTO.CategoryDto;
import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.CategoryOffer;
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
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryOfferService categoryOfferService;

    @GetMapping
    public String showCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("newCategory", new Category()); // Add an empty category for the form
        return "Admin/dashBoard/categories/categories";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("newCategory") CategoryDto categoryDto,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              Model model) {
        if (categoryService.categoryExistsByName(categoryDto.getName())) {
            model.addAttribute("categoryExistsError", "Category with this name already exists");
            return showCategories(model); // Redirect back to the category list page
        }

        // Set image data from the uploaded file
        try {
            byte[] imageData = imageFile.getBytes();
            categoryDto.setImageData(Base64.getEncoder().encodeToString(imageData));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }

        categoryService.saveCategory(categoryDto);
        return "redirect:/admin/categories";
    }



    @GetMapping("/enable/{categoryId}")
    public String enableCategory(@PathVariable("categoryId") Long id, RedirectAttributes ra) {
        Category category = categoryService.findById(id);
        String name= category.getName();
        try {
            if (category!=null){
                boolean lock=category.isActive();
                category.setActive(!lock);
                ra.addFlashAttribute("message", "The category " + name + " has been " + (lock ? "locked." : "unlocked."));
            }
        }
        catch (UsernameNotFoundException e){
            ra.addFlashAttribute("message", e.getMessage());
        }
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{categoryId}")
    public String deleteUser(@PathVariable("categoryId") Long id, RedirectAttributes ra) {
        Category category=categoryService.findById(id) ;
        String name= category.getName();
        try {
            categoryService.deleteCategory(id);
            ra.addFlashAttribute("message", "The category " + name + " has been deleted.");
        }catch (UsernameNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/categories";
    }

    @GetMapping("/get/{categoryId}")
    public String getUser(@PathVariable("categoryId") Long id, Model model) {
        Category category = categoryService.findById(id);;
        model.addAttribute("categoryDetails", category);
        return "Admin/dashBoard/categories/getCategoryDetails";
    }

    @PostMapping("/updateCategory/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("category") Category category, @RequestParam("imageFile") MultipartFile imageFile, RedirectAttributes ra) {
        String name = category.getName();

        try {
            // Check if a new image file is provided
            if (!imageFile.isEmpty()) {
                byte[] imageData = imageFile.getBytes();
                category.setImage(imageData);
            }

            // Update the category
            categoryService.updateCategory(id, category);

            ra.addFlashAttribute("message", "The category " + name + " has been updated.");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
            ra.addFlashAttribute("error", "An error occurred while updating the category.");
        }

        return "redirect:/admin/categories";
    }


}
