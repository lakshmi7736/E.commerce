package com.Mirra.eCommerce.Repository.User;

import com.Mirra.eCommerce.Models.Users.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepo extends JpaRepository<Address ,Integer> {

    List<Address> findByUser_Id(int userId);
}
