package com.Mirra.eCommerce.Controller.AdminController.DataController;

import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.User.UserAdditionalService;
import com.Mirra.eCommerce.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")

public class UserAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAdditionalService userAdditionalServiceService;


    @Autowired
    private UserAdditionalService additionalService;

    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userAdditionalServiceService.findByRole();
        System.out.println(" to get users ");

        for (User u: users){
            System.out.println(u.getId());
        }
        model.addAttribute("users", users);
        return "Admin/dashBoard/users/users";
    }


    @GetMapping("/users/block/{email}")
    public String blockUser(@PathVariable("email") String  email, RedirectAttributes ra) {
        User user=userService.findByEmail(email);
        try{
            if (user!=null){
                boolean lock=user.isEnable();
                user.setEnable(!lock);
                ra.addFlashAttribute("message", "The user " + email + " has been " + (lock ? "locked." : "unlocked."));

            }
        }catch (UsernameNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        userService.saveUser(user);
        return "redirect:/admin/user";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes ra) {
        User user=userService.findById(id) ;
        String email=user.getEmail();
        try {
            userService.deleteUser(user);

            ra.addFlashAttribute("message", "The user " + email + " has been deleted.");
        }catch (UsernameNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/user";
    }


}
