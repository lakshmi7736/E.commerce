package com.Mirra.eCommerce.Service.User;

import com.Mirra.eCommerce.DTO.AddressDto;
import com.Mirra.eCommerce.Models.Users.Address;

import java.util.List;

public interface AddressService {

    public AddressDto saveAddress(String username, AddressDto address) ;

    public List<Address> findAddressesByUserId(int userId);

    public Address getAddressById(int addressId);

    public void deleteAddress(Address address);
    public Address updateAddress(Address address);
}
