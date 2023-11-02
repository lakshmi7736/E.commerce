package com.Mirra.eCommerce.Service.User;

import com.Mirra.eCommerce.DTO.AddressDto;
import com.Mirra.eCommerce.Models.Users.Address;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Repository.User.AddressRepo;
import com.Mirra.eCommerce.Repository.User.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;


    @Override
    public AddressDto saveAddress(String username, AddressDto address) {
        // Validate username and address
        if (username == null || username.isEmpty() || address == null) {
            throw new IllegalArgumentException("Invalid username or address.");
        }

        // Fetch the user by username
        User user = userRepo.findByEmail(username);
        if (user != null) {
            Address userAddress = createAddressFromDto(address, user);

            addressRepo.save(userAddress);

            // Return the saved address
            return address;
        } else {
            throw new EntityNotFoundException("User not found.");
        }
    }

    private Address createAddressFromDto(AddressDto addressDto, User user) {
        return Address.builder()
                .user(user)
                .active(true)
                .address(addressDto.getAddress())
                .postcode(addressDto.getPostcode())
                .city(addressDto.getCity())
                .state(addressDto.getState())
                .country(addressDto.getCountry())
                .build();
    }


    @Override
    public List<Address> findAddressesByUserId(int userId) {
        return addressRepo.findActiveUserAddressByUser_Id(userId);
    }

    @Override
    public Address getAddressById(int addressId) {
        // Use the repository to retrieve the address by its ID
        return addressRepo.findById(addressId).orElse(null);
    }

    @Override
    public void deleteAddress(Address address) {
        address.setActive(false);
        addressRepo.save(address);
    }

    @Override
    public Address updateAddress(Address address) {
        return addressRepo.save(address);
    }
}
