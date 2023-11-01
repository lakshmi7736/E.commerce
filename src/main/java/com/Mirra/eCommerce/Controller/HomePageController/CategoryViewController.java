package com.Mirra.eCommerce.Controller.HomePageController;


import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Models.datas.SubCategory;
import com.Mirra.eCommerce.Service.Category.CategoryService;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
import com.Mirra.eCommerce.Service.Product.ProductService;
import com.Mirra.eCommerce.Service.Product.ProductsAdditionalService;
import com.Mirra.eCommerce.Service.SubCategory.SubCategoryService;
import com.Mirra.eCommerce.Service.User.Related.CartlistService;
import com.Mirra.eCommerce.Service.User.Related.WishlistService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/category")
public class CategoryViewController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private ProductService productsService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartlistService cartlistService;

    @Autowired
    private WishlistService wishlistService;


    @Autowired
    private ProductsAdditionalService productsAdditionalService;

    @Autowired
    private SerializeAndDeserialize serializeAndDeserialize;






    @GetMapping("/{Id}/{type}")
    public String viewCategory(@PathVariable Long Id, @PathVariable String type, Model model, HttpSession session) throws IOException, ClassNotFoundException {
        commonCode(model);

        List<Product> products = new ArrayList<>();

        if ("category".equals(type)) {
            products = productsService.getProductsByCategoryId(Id);
        } else if ("subCategory".equals(type)) {
            products = productsService.getProductsBySubCategoryId(Id);

        } else {
            // Handle invalid 'type' parameter, perhaps by returning an error view
            return "errorView";
        }

        List<Product> activeProducts = products.stream()
                .filter(Product::isActive)
                .collect(Collectors.toList());
        Collections.reverse(activeProducts);

        List<String> encodedImagesList = encodeImages(activeProducts);


        model.addAttribute("products", activeProducts);
        model.addAttribute("encodedImagesList", encodedImagesList);


        int totalQuantity = 0;
        int wishListCount = 0;
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse != null) {
            String username = jwtResponse.getUsername();
            User user = userService.findByEmail(username);

            if (user != null) {
                int loggedInUserId = user.getId();
                totalQuantity = cartlistService.getCartListCountForUser(loggedInUserId);
                wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
            }
        }

        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("wishListCount", wishListCount);

        return "Products/productViewByCategory";
    }



    @GetMapping("/search")
    public String searchProducts(@RequestParam("alphabet") String alphabet, Model model,HttpSession session) throws IOException, ClassNotFoundException {
        commonCode(model);

        List<Product> products = productsAdditionalService.searchProducts(alphabet);

        List<Product> activeProducts = products.stream()
                .filter(Product::isActive)
                .collect(Collectors.toList());
        Collections.reverse(activeProducts);

        List<String> encodedImagesList = encodeImages(activeProducts);

        model.addAttribute("products", activeProducts);
        model.addAttribute("encodedImagesList", encodedImagesList);
        int totalQuantity = 0;
        int wishListCount = 0;
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse != null) {
            String username = jwtResponse.getUsername();
            User user = userService.findByEmail(username);

            if (user != null) {
                int loggedInUserId = user.getId();
                totalQuantity = cartlistService.getCartListCountForUser(loggedInUserId);
                wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
            }
        }

        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("wishListCount", wishListCount);

        return "Products/productViewByCategory";
    }


    @GetMapping("/filter/{minAmount}/{maxAmount}")
    public String filterByAmount(@PathVariable(name = "minAmount") BigDecimal minPrice, @PathVariable("maxAmount") BigDecimal maxPrice, Model model,HttpSession session) throws IOException, ClassNotFoundException {


        List<Product> products=productsAdditionalService.findProductsUnderPrice(minPrice,maxPrice);
        model.addAttribute("products", products);

        commonCode(model);


        List<String> encodedImagesList = encodeImages(products);
        model.addAttribute("encodedImagesList", encodedImagesList);
        int totalQuantity = 0;
        int wishListCount = 0;
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse != null) {
            String username = jwtResponse.getUsername();
            User user = userService.findByEmail(username);

            if (user != null) {
                int loggedInUserId = user.getId();
                totalQuantity = cartlistService.getCartListCountForUser(loggedInUserId);
                wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
            }
        }

        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("wishListCount", wishListCount);
        return "Products/productViewByCategory";
    }


    private void commonCode(Model model ){


        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("subCategories", subCategories);

        BigDecimal maxPrice = productsAdditionalService.findMaxActualPrice();
        model.addAttribute("maxPrice", maxPrice);
    }


    private List<String> encodeImages(List<Product> products) throws IOException, ClassNotFoundException {
        List<String> encodedImagesList = new ArrayList<>();
        for (Product product : products) {
            List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(product.getImageBlob());
            String encodedImage = Base64.getEncoder().encodeToString(imageDataList.get(0));
            encodedImagesList.add(encodedImage);
            if(product.getProductOffer()!=null){
                product.getProductOffer().checkExpirationDate();
            }
            if(product.getCategory().getCategoryOffer()!=null){
                product.getCategory().checkExpirationDate();
                if(product.getCategory().getCategoryOffer().getDiscountPrice()==null){
                    product.setMyPrice(BigDecimal.ZERO);
                }
            }
        }
        return encodedImagesList;
    }





}
