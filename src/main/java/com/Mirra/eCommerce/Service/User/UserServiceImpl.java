package com.Mirra.eCommerce.Service.User;

import com.Mirra.eCommerce.DTO.UserDto;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Repository.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl  implements UserService{



    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserDto userDto) {
        User user = User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .mobileNo(userDto.getMobileNo())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role("ROLE_USER")
                .enable(true)
                .accountNonLocked(true)
                .failedAttempt(0)
                .lockTime(null)
                .build();
        userRepo.save(user);
    }

    @Override
    public User updateUser(Integer id, User user) {
        User sr=userRepo.findById(id).orElse(null);
        if(sr!=null){
            sr.setId(user.getId());
            sr.setName(user.getName());
            sr.setEmail(user.getEmail());
            sr.setMobileNo(user.getMobileNo());
            userRepo.save(sr);
            return sr;
        }else{
            return  null;
        }
    }


    @Override
    public User deleteUser(User user) {
        user.setAdminDelete(false);
        return userRepo.save(user);
    }

    @Override
    public void saveUser(User user) {
        userRepo.save(user);
    }


    @Override
    public User findById(int id) {
        return userRepo.findById(id).get();
    }


    @Override
    public User findByEmail(String email) {
        return  userRepo.findByEmail(email);
    }



}
