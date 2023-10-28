package com.Mirra.eCommerce.Controller.UsersController;

import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Address;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.User.AddressService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/user")
@Controller
public class UserProfileController {


    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");
        if (jwtResponse != null) {
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
        }else {
            System.out.println("NULL");
        }
        return "User/UserProfile";
    }
}
