package com.Mirra.eCommerce.Service.User;

import com.Mirra.eCommerce.DTO.UserDto;
import com.Mirra.eCommerce.Models.Users.User;

import java.util.List;

public interface UserService {

    //for registration of user
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    public User updateUser(Integer id, User user);

    User findById(int id);


}
