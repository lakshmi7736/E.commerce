package com.Mirra.eCommerce.Controller.AdminController.EcomController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contact")
public class CompanyContactController {

    @GetMapping
    public String contact(){
        return "contact";
    }
}
