package com.Mirra.eCommerce.Controller.UsersController;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.Orders.OrderService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/user")
public class OrderViewController {


    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;


    @GetMapping("/myOrders")
    public String myOrders(Model model, HttpSession session) {
        JwtResponse jwtResponse = (JwtResponse) session.getAttribute("jwtResponse");

        if (jwtResponse == null) {
            return "redirect:/signin";
        }
            String username = jwtResponse.getUsername();
            User user = userService.findByEmail(username);


            // Assuming you have a service method to retrieve orders
            List<Order> orders = orderService.findOrdersByUserId(user.getId()); // Modify this based on your needs
            // Reverse the order of the 'orders' list
            Collections.reverse(orders);

            model.addAttribute("orders", orders);

        return "User/Related/myOrders";
    }
}
