package com.Mirra.eCommerce.Service.User.Related;

import com.Mirra.eCommerce.Models.Users.Related.WishList;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Repository.Datas.ProductRepository;
import com.Mirra.eCommerce.Repository.User.Related.WishlistRepository;
import com.Mirra.eCommerce.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService{

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Override
    public void addToWishlist(Long productId, String username) {
        WishList wishlist = new WishList();
        wishlist.setProducts(productRepository.findById(productId).orElse(null));
        wishlist.setUser(userService.findByEmail(username));
        wishlistRepository.save(wishlist);
    }

    @Override
    public List<WishList> getWishListByUserId(int userId) {
       return wishlistRepository.findByUserId(userId);
    }



    @Override
    public int getWishListCountForUser(int user) {
        return wishlistRepository.countByUser_Id(user);
    }

    @Override
    public void removeCartItem(int wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }

    @Override
    public boolean existsByUserAndProduct(User user, Product products) {
        return wishlistRepository.existsByUserAndProducts(user,products);
    }

    @Override
    public void clearWishlistByUser(User user) {
        wishlistRepository.deleteWishlistByUser(user);
    }


}
