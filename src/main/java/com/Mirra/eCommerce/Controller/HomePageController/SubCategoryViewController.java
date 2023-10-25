package com.Mirra.eCommerce.Controller.HomePageController;

import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Models.datas.SubCategory;
import com.Mirra.eCommerce.Service.Category.CategoryService;
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
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/subCategory")
public class SubCategoryViewController {

    @Autowired
    private ProductService productsService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private SerializeAndDeserialize serializeAndDeserialize;

    @GetMapping("/{subCategoryId}")
    public String viewSubCategoryProducts(
            @PathVariable Long subCategoryId,
            Model model
    ) throws IOException, ClassNotFoundException {

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        List<SubCategory> subCategories= subCategoryService.getAllSubCategories();
        model.addAttribute("subCategories",subCategories);

        // Fetch products for the selected subcategory
        List<Product> products = productsService.getProductsBySubCategoryId(subCategoryId);
        Collections.reverse(products);

        // Convert image data to encoded strings as you did before
        List<String> encodedImagesList = new ArrayList<>();
        for (Product product : products) {
            List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());
            String encodedImage = Base64.getEncoder().encodeToString(imageDataList.get(0));
            encodedImagesList.add(encodedImage);
        }

        // Add the products and encoded images to the model
        model.addAttribute("products", products);
        model.addAttribute("encodedImagesList", encodedImagesList);

        return "Products/productViewBySubCategory";
    }
}
