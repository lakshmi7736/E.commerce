package com.Mirra.eCommerce.Controller.AdminController;

import com.Mirra.eCommerce.DTO.OTP.OtpRequest;
import com.Mirra.eCommerce.DTO.OTP.OtpResponseDto;
import com.Mirra.eCommerce.DTO.OTP.OtpValidationRequest;
import com.Mirra.eCommerce.JwtSecurity.JwtHelper;
import com.Mirra.eCommerce.Models.Token.JwtResponse;
import com.Mirra.eCommerce.Models.Token.RefreshToken;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.SmsService;
import com.Mirra.eCommerce.Service.Token.RefreshTokenService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/otp")
@Slf4j
public class OtpController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @GetMapping("/OtpLogin")
    public String OtpLogin(Model model) {
        OtpRequest otpRequest = new OtpRequest();
        model.addAttribute("otp", otpRequest);
        return "OtpLogin";
    }

    @PostMapping("/send-otp")
    public String sendOtp(@ModelAttribute("otp") @RequestBody OtpRequest otpRequest, Model model, RedirectAttributes redirectAttributes) {
        log.info("inside sendOtp :: " + otpRequest.getUsername());
        System.out.println("INSIDE POST SEND OTP");
        User user = userService.findByEmail(otpRequest.getUsername());

        if (user == null) {
            model.addAttribute("error", "User not registered.");
            return "OtpLogin";
        }

        OtpRequest otpRequest1 = OtpRequest.builder()
                .username(otpRequest.getUsername())
                .phoneNumber(user.getMobileNo())
                .build();
        System.out.println(otpRequest1);

        try {
            boolean otpSent = smsService.sendSMS(otpRequest1);
            if (otpSent) {
                // Add the username as a parameter in the redirect URL
                redirectAttributes.addAttribute("username", otpRequest.getUsername());
                return "redirect:/otp/verifyOtp"; // Redirect to "verifyOtp" page along with the username parameter
            } else {
                model.addAttribute("error", "Failed to send OTP.");
                return "OtpLogin";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error sending OTP: " + e.getMessage());
            return "OtpLogin";
        }
    }


    @GetMapping("/verifyOtp")
    public String verifyOtp(@RequestParam("username") String username, Model model) {
        System.out.println("GET USERS EMIL " + username);
        System.out.println("inside get verify OTP");
        OtpValidationRequest otp = new OtpValidationRequest();
        otp.setUsername(username);
        model.addAttribute("verify", otp);
        return "verifyOtp";
    }


    @PostMapping("/validate-otp")
    public String validateOtp(@ModelAttribute("verifyOtp") @RequestBody OtpValidationRequest otpValidationRequest, Model model, HttpServletRequest request) {
        log.info("inside validateOtp :: " + otpValidationRequest.getUsername() + " " + otpValidationRequest.getOtpNumber());
        try {
            boolean otpSent = smsService.validateOtp(otpValidationRequest);
            if (otpSent) {

                UserDetails userDetails1 = userDetailsService.loadUserByUsername(otpValidationRequest.getUsername());
                User user=userService.findByEmail(otpValidationRequest.getUsername());
                String token = helper.generateToken(userDetails1);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(otpValidationRequest.getUsername());

                JwtResponse response1 = JwtResponse.builder()
                        .jwtToken(token)
                        .username(userDetails1.getUsername())
                        .name(user.getName())
                        .refreshToken(refreshToken.getRefreshToken())
                        .build();

                HttpSession session = request.getSession();
                session.setAttribute("jwtResponse", response1);
                return "redirect:/"; // Redirect to "verifyOtp" page along with the username parameter
            } else {
                model.addAttribute("error", "INVALID OTP."); // Add error message to the model
                return "OtpLogin"; // Redirect to "OtpLogin" page with error message
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error sending OTP: " + e.getMessage()); // Add error message to the model
            return "OtpLogin"; // Redirect to "OtpLogin" page with error message
        }
    }


    @PostMapping("/forgotPass")
    public String forgotPass(@ModelAttribute("verifyOtp") @RequestBody OtpValidationRequest otpValidationRequest, Model model) {
        log.info("inside validateOtp :: " + otpValidationRequest.getUsername() + " " + otpValidationRequest.getOtpNumber());
        try {
            boolean otpSent = smsService.validateOtp(otpValidationRequest);
            if (otpSent) {
                return "redirect:/"; // Redirect to "verifyOtp" page along with the username parameter
            } else {
                model.addAttribute("error", "INVALID OTP."); // Add error message to the model
                return "OtpLogin"; // Redirect to "OtpLogin" page with error message
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error sending OTP: " + e.getMessage()); // Add error message to the model
            return "OtpLogin"; // Redirect to "OtpLogin" page with error message
        }
    }
}