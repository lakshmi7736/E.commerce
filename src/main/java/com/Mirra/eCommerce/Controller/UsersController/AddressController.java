package com.Mirra.eCommerce.Controller.UsersController;

import com.Mirra.eCommerce.DTO.AddressDto;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.Address;
import com.Mirra.eCommerce.Service.User.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/user")
@Controller
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/address")
    public String address(Model model, HttpServletRequest request){
        Address address= new Address();
        model.addAttribute("addAddress", address);
        // Get the referrer URL from the HTTP request headers
        String referrer = request.getHeader("referer");
        model.addAttribute("referrerUrl", referrer); // Add referrer URL to the model
        return "User/Address";
    }

    @PostMapping("address")
    public String saveAddress(
            @RequestParam("referrer") String referrer,
            @Valid @ModelAttribute("addAddress") AddressDto address,
            HttpSession session
    ) {

        System.out.println("referal :"+referrer);
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");
        if (jwtResponse != null) {

            String username = jwtResponse.getUsername();

            addressService.saveAddress(username, address);
        }

        // Redirect back to the referrer URL
        return "redirect:" + referrer;
    }

}
