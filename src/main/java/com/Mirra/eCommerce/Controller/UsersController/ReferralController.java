package com.Mirra.eCommerce.Controller.UsersController;

import com.Mirra.eCommerce.DTO.Coupons.ReferralDto;
import com.Mirra.eCommerce.DTO.Coupons.ReferralPointsDto;
import com.Mirra.eCommerce.DTO.UserDto;
import com.Mirra.eCommerce.Models.Coupons.Referral;
import com.Mirra.eCommerce.Models.Coupons.ReferralPoints;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Related.Wallet;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.Coupons.ReferralPointsService;
import com.Mirra.eCommerce.Service.Coupons.ReferralService;
import com.Mirra.eCommerce.Service.User.Related.WalletService;
import com.Mirra.eCommerce.Service.User.UserAdditionalService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/user/referral")
public class ReferralController {


    @Autowired
    private UserService userService;

    @Autowired
    private ReferralService referralService;

    @Autowired
    private ReferralPointsService referralPointsService;

    @Autowired
    private UserAdditionalService userAdditionalService;

    @Autowired
    private WalletService walletService;


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


    @GetMapping("/referralRegister")
    public String referralRegister(Model model){
        User user= new User();
        model.addAttribute("user",user);
        return "referralRegister";
    }

    @PostMapping("saveUser")
    public String registrationAdmin(
            @Valid @ModelAttribute("user") UserDto userDto,
            @RequestParam(value = "code",required = false) String code,
            BindingResult result,
            Model model) {

        // Check if the 'code' parameter is provided and not empty
        if (code == null || code.isEmpty()) {
            return "redirect:/user/referral/referralRegister?error";
        }

        if(! referralService.codeExistsByName(code)){
            return "redirect:/user/referral/referralRegister?error";
        }

        // Check if the provided referral code exists
        User referringUser = referralService.findUserByCode(code);
        if (referringUser == null) {
            return "redirect:/user/referral/referralRegister?error";
        }

        if (result.hasErrors()) {
            return "referralRegister"; // Return to the registration form with validation errors.
        }

        boolean validNameType=userAdditionalService.isValidName(userDto.getName());
        if(!validNameType){
            result.rejectValue("name", null, "Please enter a valid name.");
            return "referralRegister"; // Return to the registration form with the email error message.
        }


        String email = userDto.getEmail();
        User existingUser = userService.findByEmail(email);
        if (existingUser != null) {
            result.rejectValue("email", null, "User already registered !!!");
            return "referralRegister"; // Return to the registration form with the email error message.
        }

        // Check if email is of valid type
        boolean validEmailType = userAdditionalService.isValidEmail(userDto.getEmail());
        if (!validEmailType) {
            result.rejectValue("email", null, "Invalid email format. Only Gmail addresses are allowed.");
            return "referralRegister"; // Return to the registration form with the email error message.
        }

        boolean validPhoneNumber = userAdditionalService.isValidPhoneNumber(userDto.getMobileNo());
        if (!validPhoneNumber) {
            result.rejectValue("mobileNo", null, "Invalid phone number format. Please enter a valid 10-digit number.");
            return "referralRegister"; // Return to the registration form with the phone number error message.
        }

        ReferralPoints points=referralPointsService.findAnyReferralPoints();



        userService.saveUser(userDto);
        // Create a wallet for the newly saved user
        Wallet newWallet = new Wallet();
        newWallet.setUser(userService.findByEmail(userDto.getEmail())); // Retrieve the saved user
        newWallet.setAmount(BigDecimal.valueOf(points.getActivatedBonus())); // Set the initial amount
        walletService.save(newWallet);
        referralAmount(referringUser,points);
        return "redirect:/user/referral/referralRegister?success";
    }

    public void referralAmount(User referringUser,ReferralPoints points){
        Wallet existingWallet = walletService.findByUser(referringUser);

        if (existingWallet == null) {
            // If the user doesn't have a wallet, create a new one
            Wallet newWallet = new Wallet();
            newWallet.setUser(referringUser);
            newWallet.setAmount(BigDecimal.valueOf(points.getReferralBonus()));
            walletService.save(newWallet);
        } else {

            // If the user already has a wallet, update the existing wallet
            BigDecimal currentAmount = existingWallet.getAmount();
            if(currentAmount==null){
                currentAmount=BigDecimal.ZERO;
            }
            existingWallet.setAmount(currentAmount.add(BigDecimal.valueOf(points.getReferralBonus()))); // Add the canceled order's total amount to the wallet
            walletService.save(existingWallet);
        }
    }

}
