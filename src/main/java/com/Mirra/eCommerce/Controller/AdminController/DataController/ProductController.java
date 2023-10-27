package com.Mirra.eCommerce.Controller.AdminController.DataController;

import com.Mirra.eCommerce.DTO.ProductUpdateRequest;
import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Models.datas.SubCategory;
import com.Mirra.eCommerce.Service.Category.CategoryService;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
import com.Mirra.eCommerce.Service.Product.CalculateAverageRatingService;
import com.Mirra.eCommerce.Service.Product.ProductReviewService;
import com.Mirra.eCommerce.Service.Product.ProductService;
import com.Mirra.eCommerce.Service.SubCategory.SubCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/product")
public class ProductController {


    private static final long MAX_IMAGE_SIZE = 1024 * 1024 * 1024; // 1 GB (adjust the size as needed)

    @Autowired
    private ProductService productsService;

    @Autowired
    private SerializeAndDeserialize serializeAndDeserialize;

    @Autowired
    private CalculateAverageRatingService calculateAverageRatingService;

    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping
    public String productForm(Model model) {
        List<Category> categories = categoryService.getAllCategories(); // Assuming you have a service method for retrieving categories
        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();

        model.addAttribute("categories", categories); // Add categories to the model
        model.addAttribute("subCategories", subCategories);
        model.addAttribute("product", new Product());
        return "Admin/dashBoard/products/products";
    }

//    create product
    @PostMapping
    public String saveProduct(@Valid @ModelAttribute Product product,
                              BindingResult bindingResult,
                              @RequestParam("files") MultipartFile[] files,
                              Model model) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors (e.g., show error messages)
            return handleValidationErrors(model);
        }

        List<byte[]> imageDataList = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                if (file.getSize() > MAX_IMAGE_SIZE) {
                    return handleImageSizeExceeded();
                }

                try {
                    resizeAndAddImage(imageDataList, file);
                } catch (IOException e) {
                    handleImageProcessingError(e);
                }
            }
        }

        try {
            processAndSaveProduct(product, imageDataList);
        } catch (Exception e) {
            handleProductSaveError(e);
        }

        return "redirect:/admin/product";
    }

    private String handleValidationErrors(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("subCategories", subCategories);
        return "Admin/dashBoard/products/products";
    }

    private void resizeAndAddImage(List<byte[]> imageDataList, MultipartFile file) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
        int maxWidth = 800; // Adjust the maximum width as needed
        int maxHeight = 800; // Adjust the maximum height as needed
        // ... Image resizing logic (if required) ...
        imageDataList.add(file.getBytes());
    }

    private void processAndSaveProduct(Product product, List<byte[]> imageDataList) throws Exception {
        byte[] imageBlob = serializeAndDeserialize.serializeImageList(imageDataList);
        product.setImageBlob(imageBlob);
        product.setStock(product.getStock() + product.getUpdateStock());
        product.setActive(true);
        product.setMyPrice(BigDecimal.ZERO);
        productsService.saveProduct(product);
    }

    private String handleImageSizeExceeded() {
        // Handle large image size, e.g., show an error message to the user
        // or resize/compress the image before saving
        return "redirect:/product?error=Image size exceeds the allowed limit.";
    }

    private void handleImageProcessingError(Exception e) {
        // Handle the exception (e.g., logging, displaying an error message)
        e.printStackTrace();
    }

    private void handleProductSaveError(Exception e) {
        // Handle the exception (e.g., logging, displaying an error message)
        e.printStackTrace();
    }



//    view product Lists
    @GetMapping("/productList")
    public String productList(Model model) throws IOException, ClassNotFoundException {
        List<Product> products = productsService.getAllProducts();
        // Filter users where adminDelete is true
        List<Product> productsList = products.stream()
                .filter(Product::isActive)
                .collect(Collectors.toList());

        model.addAttribute("products", productsList);
        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("subCategories", subCategories);

        List<String> encodedImagesList = new ArrayList<>();
        for (Product product : productsList) {
            List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());
            String encodedImage = Base64.getEncoder().encodeToString(imageDataList.get(0));
            encodedImagesList.add(encodedImage);
        }

        model.addAttribute("encodedImagesList", encodedImagesList);

        return "Admin/dashBoard/products/productList"; // Return the name of the view template for the product list page
    }


//    view out of stock products
    @GetMapping("/productStockOutList")
    public String productStockOutList(Model model) throws IOException, ClassNotFoundException {
        List<Product> products = productsService.getAllProducts();

        // Filter products where stock is 0
        List<Product> productsList = products.stream()
                .filter(product -> !product.isActive() && product.getStock() == 0)
                .collect(Collectors.toList());

        model.addAttribute("products", productsList);

        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("subCategories", subCategories);

        List<String> encodedImagesList = new ArrayList<>();
        for (Product product : productsList) {
            List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());
            String encodedImage = Base64.getEncoder().encodeToString(imageDataList.get(0));
            encodedImagesList.add(encodedImage);
        }

        model.addAttribute("encodedImagesList", encodedImagesList);

        return "Admin/dashBoard/products/productList"; // Return the name of the view template for the product list page
    }

//    disable productById
    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId, RedirectAttributes ra) {
        Product product = productsService.getProductById(productId);
        if (product == null) {
            // Handle product not found case, e.g., redirect to an error page
            return "error";
        }
        ra.addFlashAttribute("message", "The  " + product.getName() + " has been deleted");

        // Delete the product from the database
        productsService.deleteProduct(product);

        return "redirect:/admin/product/productList";
    }


    @GetMapping("/edit/{productId}")
    public String editProductForm(@PathVariable Long productId, Model model) throws IOException, ClassNotFoundException {
        Product product = productsService.getProductById(productId);
        if (product == null) {
            // Handle product not found case, e.g., redirect to an error page
            return "error";
        }
        model.addAttribute("product", product);

        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("subCategories", subCategories);


        // Deserialize the image data from the product's imageBlob attribute
        List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());

        // Encode the image data as Base64 strings
        List<String> encodedImages = new ArrayList<>();
        for (byte[] imageData : imageDataList) {
            String encodedImage = Base64.getEncoder().encodeToString(imageData);
            encodedImages.add(encodedImage);
        }
        model.addAttribute("encodedImages", encodedImages);

        return "Admin/dashBoard/products/ProductUpdateForm";
    }

//    updtae product

    @PostMapping("/edit/{productId}")
    public String updateProduct(@PathVariable Long productId,
                                @ModelAttribute ProductUpdateRequest request,
                                RedirectAttributes ra) {
        try {
            Product product = productsService.getProductById(productId);
            if (product == null) {
                return "error"; // Handle product not found
            }

            SubCategory newSubCategory = subCategoryService.getSubCategoryById(request.getSubCategoryId());
            if (newSubCategory == null) {
                return "error"; // Handle sub-category not found
            }


             resetPrice(product, request.getActualPrice());


            updateStockInformation(product, request);

            updateProductDetails(product, request, newSubCategory);


            updateImages(product, request.getNewImages());


            ra.addFlashAttribute("message", "The " + product.getName() + " has been updated");

            productsService.saveProduct(product);
        } catch (Exception e) {
            // Handle exceptions and return an error page
            return "error";
        }

        return "redirect:/admin/product/productList";
    }


    private void resetPrice(Product product, BigDecimal actualPrice) {
        product.setActualPrice(actualPrice);
        product.setMyPrice(BigDecimal.ZERO);
    }

    private void updateProductDetails(Product product, ProductUpdateRequest request, SubCategory newSubCategory) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setSubCategory(newSubCategory);
        product.setStock(product.getStock() + request.getUpdateStock());
        product.setUpdateStock(request.getUpdateStock());
        if (request.getUpdateStock() > 0) {
            product.setActive(true);
        }
    }

    private void updateStockInformation(Product product, ProductUpdateRequest request) {
        if (request.getUpdateStock() != 0) {
            product.updateStock(request.getUpdateStock());
            product.currentStock(product.getStock());
        }
    }

    private void updateImages(Product product, MultipartFile[] newImages) throws IOException, ClassNotFoundException {
        try {
            List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());
            for (MultipartFile newImage : newImages) {
                if (!newImage.isEmpty()) {
                    byte[] newImageData = newImage.getBytes();
                    imageDataList.add(newImageData);
                }
            }
            product.setImageBlob(serializeAndDeserialize.serializeImageList(imageDataList));
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions and log the error
        }
    }



}
