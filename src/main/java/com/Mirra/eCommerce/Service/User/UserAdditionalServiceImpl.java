package com.Mirra.eCommerce.Service.User;

import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Repository.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserAdditionalServiceImpl implements UserAdditionalService{

    public static final long LOCK_TIME_DURATION = 100000;  //1 min

    @Autowired
    private UserRepo userRepo;



    @Override
    public boolean isValidEmail(String email) {
        // Define the pattern to match a valid email address
        String emailRegex = "^[\\w\\.-]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(emailRegex);

        // Match the given email address with the pattern
        Matcher matcher = pattern.matcher(email);

        // Return true if the email address matches the pattern, false otherwise
        return matcher.matches();
    }


    @Override
    public boolean isValidPhoneNumber(String phoneNumber) {
        String indianPhoneNumberRegex = "^(?:(?:\\+|0{0,2})91(\\s*[-]\\s*)?|[0]?)?[6789]\\d{9}$";
        Pattern pattern = Pattern.compile(indianPhoneNumberRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    @Override
    public boolean isValidName(String name) {
        // Define the pattern to match a valid name (no special characters except underscore)
        String nameRegex = "^[a-zA-Z0-9_]+( [a-zA-Z0-9_]+)*(\\.[a-zA-Z0-9_]+)?$";

        // Check if the name is not empty and matches the pattern
        return !name.isEmpty() && name.matches(nameRegex);
    }


    @Override
    public void increaseFailedAttempts(User user) {
        int failedAttempts = user.getFailedAttempt() + 1;
        userRepo.updateFailedAttempt(failedAttempts, user.getEmail());
    }

    @Override
    public void lockUser(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(LocalDateTime.now());
        userRepo.save(user);
    }

    @Override
    public boolean unlockUser(User user) {
        LocalDateTime lockTime = user.getLockTime();
        if (lockTime != null) {
            long lockTimeInMills = lockTime.toInstant(ZoneOffset.UTC).toEpochMilli();
            long currentTimeMillis = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
            long unlockTimeMillis = lockTimeInMills + LOCK_TIME_DURATION;

            System.out.println(currentTimeMillis + " > " + unlockTimeMillis);
            if (currentTimeMillis > unlockTimeMillis) {
                user.setAccountNonLocked(true);
                user.setLockTime(null);
                user.setFailedAttempt(0);
                userRepo.save(user);
                return true;
            }
        }
        return false;
    }
    @Override
    public void resetFailedAttempts(String email) {
        System.out.println(email);
        userRepo.updateFailedAttempt(0, email);
    }

    @Override
    public List<User> getExpiredLockedUsers() {
        LocalDateTime currentTime = LocalDateTime.now();
        return userRepo.findExpiredLockedUsers(currentTime);
    }

    @Override
    public List<User> findByRole() {
        return userRepo.findByRole("ROLE_USER");
    }
}
