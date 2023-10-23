package com.Mirra.eCommerce.Controller.HomePageController;

import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Models.datas.SubCategory;
import com.Mirra.eCommerce.Service.Category.CategoryService;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/category/{categoryId}")
public class CategoryViewController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private ProductService productsService;

    @Autowired
    private SerializeAndDeserialize serializeAndDeserialize;


    @GetMapping
    public String viewCategory(@PathVariable Long categoryId, Model model) throws IOException, ClassNotFoundException {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        List<SubCategory> subCategories= subCategoryService.getAllSubCategories();
        model.addAttribute("subCategories",subCategories);

        List<Product> products = productsService.getProductsByCategoryId(categoryId);

        List<Product> activeProducts = products.stream()
                .filter(product -> product.isActive())
                .collect(Collectors.toList());

        model.addAttribute("products", activeProducts);


        Collections.reverse(products);
        List<String> encodedImagesList = new ArrayList<>();

        for (Product product : activeProducts) {
            List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());
            String encodedImage = Base64.getEncoder().encodeToString(imageDataList.get(0));
            encodedImagesList.add(encodedImage);
            product.checkExpirationDate();
        }
        model.addAttribute("encodedImagesList", encodedImagesList);
        return "Products/productView";
    }
}
