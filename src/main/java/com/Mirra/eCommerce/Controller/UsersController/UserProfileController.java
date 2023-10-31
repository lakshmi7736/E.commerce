package com.Mirra.eCommerce.Controller.UsersController;

import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Address;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.User.AddressService;
import com.Mirra.eCommerce.Service.User.UserAdditionalService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/user")
@Controller
public class UserProfileController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserAdditionalService userAdditionalService;

    @Autowired
    private AddressService addressService;


//    show user profile
    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse == null) {
            return "redirect:/signin";
        }
            String username = jwtResponse.getUsername();
            User user = userService.findByEmail(username);

            // Retrieve the user's addresses using the user's id
            List<Address> addresses = addressService.findAddressesByUserId(user.getId());
            // Filter the addresses to include only active ones
            List<Address> activeAddresses = addresses.stream()
                    .filter(Address::isActive)
                    .collect(Collectors.toList());

            model.addAttribute("addresses", activeAddresses); // Add the list of active addresses to the model
            model.addAttribute("user", user); // Add the user object to the model

        return "User/UserProfile";
    }





//    to edit showimg details of user
    @GetMapping("/get/{id}")
    public String getUser(@PathVariable("id") Integer id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("userDetails", user); // Note the change in the attribute name

        return "User/UpdateUserDetailsByUser";
    }


//    to update user details
    @PostMapping("/updateUser/{id}")
    public String updateUser(
            @PathVariable Integer id,
            @ModelAttribute("userDetails") User user,
            Model model,
            RedirectAttributes ra,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            // If there are validation errors, stay on the same page and return the form with errors.
            // You don't need to use a redirect for this.
            return "Admin/updateByUser"; // Return to the same page with the name error message.
        }

        boolean validNameType = userAdditionalService.isValidName(user.getName());
        if (!validNameType) {
            result.rejectValue("name", null, "Please enter a valid name.");
            return "Admin/updateByUser"; // Return to the same page with the name error message.
        }

        String email = user.getEmail();
        User existingUser = userService.findByEmail(email);
        if (existingUser != null && existingUser.getId() != id) {
            result.rejectValue("email", null, "User already registered !!!");
            return "Admin/updateByUser"; // Return to the same page with the name error message.
        }

        // Check if email is of valid type
        boolean validEmailType = userAdditionalService.isValidEmail(user.getEmail());
        if (!validEmailType) {
            result.rejectValue("email", null, "Please enter a valid email.");
            return "Admin/updateByUser"; // Return to the same page with the name error message.
        }

        boolean validPhoneNumber = userAdditionalService.isValidPhoneNumber(user.getMobileNo());
        if (!validPhoneNumber) {
            result.rejectValue("mobileNo", null, "Please enter a valid phone number.");
            return "User/UpdateUserDetailsByUser"; // Return to the same page with the name error message.
        }

        try {
            userService.updateUser(id, user);
            model.addAttribute("userDetails", user);
            ra.addFlashAttribute("userMessage", "The user ID " + id + " has been updated.");
            // Redirect back to the referrer URL
            return "redirect:/user/profile";
        } catch (Exception e) {
            return "user/updateByUser"; // Return to the same page with the name error message.
        }
    }


//    to change password
    @GetMapping("/getPass/{id}")
    public String getUserPass(@PathVariable("id") Integer id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("userDetails", user); // Note the change in the attribute name

        return "User/ChangePassword";
    }


//    to save changed password
    @PostMapping("/updateUserPass/{id}")
    public String updateUserPassword(
            @PathVariable Integer id,
            RedirectAttributes ra,
            @RequestParam("password") String password,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("rePassword") String rePassword,
            Model model) {

        // Retrieve the user from the database
        User user = userService.findById(id);

        // Check if the entered current password matches the user's password
        if (!userAdditionalService.isCurrentPasswordValid(user, password)) {
            ra.addFlashAttribute("error", "Current password is incorrect");
            return "redirect:/user/getPass/" + id; // Redirect back to the form with an error message
        }

        // Check if the new password and re-entered password match
        if (!newPassword.equals(rePassword)) {
            ra.addFlashAttribute("error", "New passwords do not match");
            return "redirect:/user/getPass/" + id; // Redirect back to the form with an error message
        }

        // Update the user's password
        userAdditionalService.updateUserPassword(user, newPassword);

        ra.addFlashAttribute("userMessage", "Password changed successfully.");

        return "redirect:/user/profile";
    }

}
