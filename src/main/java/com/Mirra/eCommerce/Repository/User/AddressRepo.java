package com.Mirra.eCommerce.Repository.User;

import com.Mirra.eCommerce.Models.Users.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<Address ,Integer> {
}
