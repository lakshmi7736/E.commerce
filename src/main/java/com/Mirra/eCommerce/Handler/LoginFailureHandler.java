package com.Mirra.eCommerce.Handler;


import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.User.UserAdditionalService;
import com.Mirra.eCommerce.Service.User.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private UserService userService;
    @Autowired
    private UserAdditionalService userAdditionalService;

    @Autowired
    public LoginFailureHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        String email = request.getParameter("username");
        User user = userService.findByEmail(email);

        if (user != null) {
            if (user.isEnable() && user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < 2) {
                    userAdditionalService.increaseFailedAttempts(user);
                } else {
                    userAdditionalService.lockUser(user);
                    exception = new LockedException("Your account has been locked due to too many login attempts. Please try again after one minute.");
                    request.getSession().setAttribute("lockedExceptionMessage", exception.getMessage());
                }
            } else if (!user.isAccountNonLocked()) {
                boolean unlocked = userAdditionalService.unlockUser(user);
                if (unlocked) {
                    exception = new LockedException("Your account has been unlocked. Please try to login again.");
                    request.getSession().setAttribute("lockedExceptionMessage", exception.getMessage());
                }
            }
        }

        // Redirect to the login page with an error parameter
        response.sendRedirect("/signin?error");
    }
}
