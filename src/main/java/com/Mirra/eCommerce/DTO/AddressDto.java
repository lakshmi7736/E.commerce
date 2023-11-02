package com.Mirra.eCommerce.DTO;

import com.Mirra.eCommerce.Models.Users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {


    private int id;

    private String country;

    private String state;

    private String city;

    private String address;

    private String postcode;

    private boolean active;

    private User user;
}
