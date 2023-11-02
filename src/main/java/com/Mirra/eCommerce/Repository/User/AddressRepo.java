package com.Mirra.eCommerce.Repository.User;

import com.Mirra.eCommerce.Models.Users.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepo extends JpaRepository<Address ,Integer> {

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.active = true")
    List<Address> findActiveUserAddressByUser_Id(@Param("userId") int userId);
}
