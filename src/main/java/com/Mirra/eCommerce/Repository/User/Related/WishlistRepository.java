package com.Mirra.eCommerce.Repository.User.Related;

import com.Mirra.eCommerce.Models.Users.Related.WishList;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishList,Integer> {

    List<WishList> findByUserId(int userId);


    boolean existsByUserAndProducts(User user, Product products);

    int countByUser_Id(int userId);;
}
