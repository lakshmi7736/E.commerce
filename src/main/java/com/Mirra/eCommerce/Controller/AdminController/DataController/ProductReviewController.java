package com.Mirra.eCommerce.Controller.AdminController.DataController;

import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Models.datas.ProductReview;
import com.Mirra.eCommerce.Service.Product.ProductReviewService;
import com.Mirra.eCommerce.Service.Product.ProductService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/message")
public class ProductReviewController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productsService;

    @Autowired
    private ProductReviewService productReviewService;

    @PostMapping("/submit-review")
    public String submitReview(@ModelAttribute("review") ProductReview review,
                               HttpSession session,
                               @RequestParam("product_id") Long productId) {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse == null) {
            // Redirect to a login page or display an error message.
            return "redirect:/signin";
        }

        User user = userService.findByEmail(jwtResponse.getUsername());
        Product product = productsService.getProductById(productId);


        review.setUser(user);
        review.setProduct(product);
        review.setReviewDate(LocalDateTime.now());

        productReviewService.saveReview(review);

        return "redirect:/productView/" + productId;
    }

}
