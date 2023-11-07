package com.Mirra.eCommerce.Controller.AdminController;


import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Service.Orders.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashBoardController {


    @Autowired
    private OrderService orderService;
    @GetMapping("/dashBoard")
    public String adminDashBoard(){
        return "Admin/admindashBoard";
    }


    @GetMapping("/users")
    public String users(){
        return "Admin/dashBoard/users/users";
    }




    @GetMapping("/ordersController")
    public String checkoutPage(Model model) {

        // Assuming you have a service method to retrieve orders
        List<Order> orders = orderService.getAllOrders(); // Modify this based on your needs
        model.addAttribute("orders", orders);

        return "Admin/dashBoard/orders/OderManagement";
    }
}
