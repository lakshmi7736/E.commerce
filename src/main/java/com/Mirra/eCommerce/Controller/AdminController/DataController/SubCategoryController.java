package com.Mirra.eCommerce.Controller.AdminController.DataController;

import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.SubCategory;
import com.Mirra.eCommerce.Service.Category.CategoryService;
import com.Mirra.eCommerce.Service.SubCategory.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/subCategories")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String showSubCategories(Model model){
        List<SubCategory> subCategories= subCategoryService.getAllSubCategories();
        List<Category> categories= categoryService.getAllCategories();
        model.addAttribute("subCategories",subCategories);
        model.addAttribute("categories",categories);
        model.addAttribute("newSubCategory", new SubCategory()); // Add an empty subCategory for the form
        return "Admin/dashBoard/subCategories/subCategories";
    }

    @PostMapping
    public String addSubCategory(@ModelAttribute("newSubCategory") SubCategory subCategory, Model model) {
        System.out.println("POST");
        Category parentCategory = subCategory.getParentCategory();

        if (subCategoryService.subCategoryExistsByNameAndParentCategory(subCategory.getName(), parentCategory)) {
            model.addAttribute("subCategoryExistsError", "Sub-Category with this name already exists for the selected category");
            return showSubCategories(model);
        }

        subCategoryService.saveSubCategory(subCategory);
        return "redirect:/admin/subCategories";
    }

    @GetMapping("/{categoryId}")
    @ResponseBody
    public List<SubCategory> getSubcategoriesByCategory(@PathVariable Long categoryId) {
        return subCategoryService.getSubcategoriesByCategoryId(categoryId);
    }


    @GetMapping("/delete/{subCategoryId}")
    public String deleteUser(@PathVariable("subCategoryId") Long id, RedirectAttributes ra) {
        SubCategory subCategory=subCategoryService.findById(id) ;
        String name= subCategory.getName();
        try {
            subCategoryService.deleteSubCategory(id);
            ra.addFlashAttribute("message", "The sub-category " + name + " has been deleted.");
        }catch (UsernameNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/subCategories";
    }

    @GetMapping("/get/{subCategoryId}")
    public String getUser(@PathVariable("subCategoryId") Long id, Model model) {
        SubCategory subCategory = subCategoryService.findById(id);
        model.addAttribute("subCategoryDetails", subCategory);

        List<Category> categories= categoryService.getAllCategories();
        model.addAttribute("categories",categories);


        return "Admin/getSubCat";
    }



    @PostMapping("/updateSubCategory/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody @ModelAttribute("subCategory") SubCategory subCategory, Model model, RedirectAttributes ra) {

        // Get the original SubCategory from the database
        SubCategory originalSubCategory = subCategoryService.findById(id);

        // Compare the original name with the updated name
        if (!originalSubCategory.getName().equals(subCategory.getName())) {
            Category parentCategory = subCategory.getParentCategory();

            if (subCategoryService.subCategoryExistsByNameAndParentCategory(subCategory.getName(), parentCategory)) {
                model.addAttribute("subCategoryExistsError", "Sub-Category with this name already exists for the selected category");
                return showSubCategories(model);
            }
        }

        // Update the SubCategory
        subCategoryService.updateSubCategory(id, subCategory);
        String name = subCategory.getName();
        model.addAttribute("subCategory", subCategory);
        ra.addFlashAttribute("message", "The sub-category " + name + " has been updated.");

        return "redirect:/admin/subCategories";
    }

    

}