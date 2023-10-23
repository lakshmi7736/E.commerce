package com.Mirra.eCommerce.DTO;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private int id;

    private String name;


    private String email;


    private String mobileNo;

    private String password;
}

