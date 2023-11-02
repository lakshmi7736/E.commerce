package com.Mirra.eCommerce.Controller.UsersController;

import com.Mirra.eCommerce.DTO.Coupons.ReferralDto;
import com.Mirra.eCommerce.DTO.Coupons.ReferralPointsDto;
import com.Mirra.eCommerce.Models.Coupons.Referral;
import com.Mirra.eCommerce.Models.Coupons.ReferralPoints;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.Coupons.ReferralPointsService;
import com.Mirra.eCommerce.Service.Coupons.ReferralService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/referral")
public class ReferralController {


    @Autowired
    private UserService userService;

    @Autowired
    private ReferralService referralService;

    @Autowired
    private ReferralPointsService referralPointsService;


    @GetMapping
    public String referral(Model model){
        Referral referral= new Referral();
        model.addAttribute("referral",referral);
        return "User/Related/CreateReferral";
    }


    @PostMapping("/add")
    public String createReferral(@RequestParam("code") String code, @ModelAttribute("referral") ReferralDto referralDto, HttpSession session, Model model) {
        // Retrieve the currently logged-in user from the HttpSession
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");
        String username = jwtResponse.getUsername();
        User user=userService.findByEmail(username);

        if (jwtResponse == null) {
            return "redirect:/signin";
        }
            if (referralService.userExist(user.getId())){
                model.addAttribute("codeExistsError", "User already has a referral code.");
                return referral(model);
            }

            if(referralService.codeExistsByName(code)){
                model.addAttribute("codeExistsError", "Referral code with this name already exists");
                return referral(model);
            }
            if(referralService.isCodeValid(code)){
                model.addAttribute("codeExistsError", "Invalid Referral code, try with another");
                return referral(model);
            }

         ReferralPoints points=referralPointsService.findAnyReferralPoints();
            referralDto.setUserId(user);
            referralDto.setReferralPointsId(points);

            referralService.saveReferral(referralDto);
            // Redirect to a success page or wherever you want
            return "redirect:/user/profile";

    }
}
