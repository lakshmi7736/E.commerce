package com.Mirra.eCommerce.Service.User.Related;

import com.Mirra.eCommerce.Models.Users.Related.WishList;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;

import java.util.List;

public interface WishlistService {
    public void addToWishlist(Long productId, String username);

    List<WishList> getWishListByUserId(int userId);

    public int getWishListCountForUser(int user);
    public void removeCartItem(int wishlistId);

    boolean existsByUserAndProduct(User user, Product products);
}
