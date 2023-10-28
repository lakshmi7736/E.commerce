package com.Mirra.eCommerce.Controller.UsersController;


import com.Mirra.eCommerce.DTO.UserDto;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.User.UserAdditionalService;
import com.Mirra.eCommerce.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAdditionalService userAdditionalService;

    @PostMapping("/userRegistration/saveUser")
    public String saveUser(@ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        System.out.println("inside controller");
        if (result.hasErrors()) {
            return "register"; // Return to the registration form with validation errors.
        }

        boolean validNameType=userAdditionalService.isValidName(userDto.getName());
        if(!validNameType){
            result.rejectValue("name", null, "Please enter a valid name.");
            return "register"; // Return to the registration form with the email error message.
        }


        String email = userDto.getEmail();
        User existingUser = userService.findByEmail(email);
        if (existingUser != null) {
            result.rejectValue("email", null, "User already registered !!!");
            return "register"; // Return to the registration form with the email error message.
        }

        // Check if email is of valid type
        boolean validEmailType = userAdditionalService.isValidEmail(userDto.getEmail());
        if (!validEmailType) {
            result.rejectValue("email", null, "Invalid email format. Only Gmail addresses are allowed.");
            return "register"; // Return to the registration form with the email error message.
        }

        boolean validPhoneNumber = userAdditionalService.isValidPhoneNumber(userDto.getMobileNo());
        if (!validPhoneNumber) {
            result.rejectValue("mobileNo", null, "Invalid phone number format. Please enter a valid 10-digit number.");
            return "register"; // Return to the registration form with the phone number error message.
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

}
