package com.Mirra.eCommerce.cron;

import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

public class UserUnlockSchedule {
    @Autowired
    private UserService userService;
    @Scheduled(cron = "0 */1 * * * *")   //Every one minutes after it's going to execute
    public void unlockUserAccount() {
        List<User> lockedUsers = userService.getExpiredLockedUsers();
        for (User user : lockedUsers) {
            System.out.println("inside cron operation"+ LocalDateTime.now());
            userService.unlockUser(user);
        }
    }
}
