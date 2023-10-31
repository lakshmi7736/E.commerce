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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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

        if (jwtResponse == null) {
            return "redirect:/signin";
        }

            String username = jwtResponse.getUsername();

            addressService.saveAddress(username, address);


        // Redirect back to the referrer URL
        return "redirect:" + referrer;
    }



    // Delete an address based on its ID
    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable("id") int addressId, RedirectAttributes redirectAttributes) {
        // Check if the address exists
        Address address = addressService.getAddressById(addressId);

        if (address == null) {
            // Address not found, handle the error (e.g., display an error message)
            redirectAttributes.addFlashAttribute("error", "Address not found.");
        } else {
            // Address found, delete it
            addressService.deleteAddress(address);
            // Optionally, add a success message
            redirectAttributes.addFlashAttribute("success", "Address deleted successfully.");
        }

        return "redirect:/user/profile";
    }




    // Display the edit address form (GET)
    @GetMapping("/edit/{id}")
    public String showEditAddressForm(@PathVariable("id") int addressId, Model model) {
        // Check if the address exists
        Address address = addressService.getAddressById(addressId);

        if (address == null) {
            // Address not found, handle the error (e.g., display an error message)
            return "redirect:/user/profile"; // Redirect to address list or an error page
        }

        // Add the address to the model to prepopulate the form
        model.addAttribute("updateAddress", address);

        return "User/updateAddress"; // Return the name of the HTML template for the edit form
    }


    @PostMapping("/editAddress/{id}")
    public String updateAddress(@PathVariable("id") int addressId,
                                @ModelAttribute("updateAddress") Address updatedAddress,RedirectAttributes redirectAttributes) {
        // Retrieve the existing address from the database by addressId
        Address existingAddress = addressService.getAddressById(addressId);

        if (existingAddress != null) {
            // Update the existing address with values from the updatedAddress object
            existingAddress.setCountry(updatedAddress.getCountry());
            existingAddress.setState(updatedAddress.getState());
            existingAddress.setCity(updatedAddress.getCity());
            existingAddress.setAddress(updatedAddress.getAddress());
            existingAddress.setPostcode(updatedAddress.getPostcode());

            // Save the updated address back to the database
            addressService.updateAddress(existingAddress);

            redirectAttributes.addFlashAttribute("success", "Address updated successfully.");

            return "redirect:/user/profile"; // Redirect to the user's profile page
        }

        // Handle the case where the address with the given ID is not found
        return "redirect:/error";
    }




}
