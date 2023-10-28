package com.Mirra.eCommerce.Service.User;

import com.Mirra.eCommerce.Models.Users.User;

import java.util.List;

public interface UserAdditionalService {

    public boolean isValidEmail(String email);

    public boolean isValidPhoneNumber(String phoneNumber);

    public boolean isValidName(String name);


    //before log-in in checking
    void increaseFailedAttempts(User user);
    void lockUser(User user);

    boolean unlockUser(User user);

    void resetFailedAttempts(String email);

    //end of log-in in checking

    List<User> getExpiredLockedUsers();

    List<User> findByRole();
}
