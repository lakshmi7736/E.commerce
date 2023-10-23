package com.Mirra.eCommerce.Controller.AdminController;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashBoardController {

    @GetMapping("/dashBoard")
    public String adminDashBoard(){
        return "Admin/admindashBoard";
    }


    @GetMapping("/users")
    public String users(){
        return "Admin/dashBoard/users/users";
    }



    @GetMapping("/orders")
    public String orders(){
        return "Admin/dashBoard/orders/orders";
    }
}
