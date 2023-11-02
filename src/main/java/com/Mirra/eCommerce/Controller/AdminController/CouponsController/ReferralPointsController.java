package com.Mirra.eCommerce.Controller.AdminController.CouponsController;

import com.Mirra.eCommerce.DTO.Coupons.ReferralPointsDto;
import com.Mirra.eCommerce.Models.Coupons.ReferralPoints;
import com.Mirra.eCommerce.Service.Coupons.ReferralPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/setReferralPoints")
public class ReferralPointsController {

    @Autowired
    private ReferralPointsService referralPointsService;

    @GetMapping
    public String setReferralPoints(Model model){
        ReferralPoints points= new ReferralPoints();
        model.addAttribute("points",points);
        return "Admin/Coupons/ReferralPoints";
    }

    @PostMapping("/add")
    public String addReferralPoints(@ModelAttribute("points") ReferralPointsDto pointsDto, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "Admin/Coupons/ReferralPoints";
        }
        referralPointsService.savePoints(pointsDto);
        return "redirect:/admin/setReferralPoints?success";
    }

}
