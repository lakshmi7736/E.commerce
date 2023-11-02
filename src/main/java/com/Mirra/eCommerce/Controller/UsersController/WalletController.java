package com.Mirra.eCommerce.Controller.UsersController;

import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Related.Wallet;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.User.Related.CartlistService;
import com.Mirra.eCommerce.Service.User.Related.WalletService;
import com.Mirra.eCommerce.Service.User.Related.WishlistService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/wallet")
public class WalletController {


    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private CartlistService cartlistService;

    @Autowired
    private WishlistService wishlistService;


    @GetMapping
    public String wallet(Model model, HttpSession session) {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse == null) {
            return "redirect:/signin";
        }
            String username = jwtResponse.getUsername();
            User user = userService.findByEmail(username);

            if (user != null) {
                int totalQuantity = 0;
                int wishListCount = 0;

                int loggedInUserId = user.getId();
                totalQuantity = cartlistService.getCartListCountForUser(loggedInUserId);
                 wishListCount = wishlistService.getWishListCountForUser(loggedInUserId);
                model.addAttribute("totalQuantity", totalQuantity);
                model.addAttribute("wishListCount", wishListCount);
                Wallet wallet = walletService.findByUser(user);

                if (wallet != null) {
                    model.addAttribute("walletAmount", wallet.getAmount());
                }
            }


        return "User/Related/Wallet";
    }

}
