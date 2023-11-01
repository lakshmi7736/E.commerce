package com.Mirra.eCommerce.Service.User.Related;

import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;

import java.util.List;

public interface CartlistService {
    public void addToCartlist(Long productId, String username);

    List<AddToCart> getCartListByUserId(int userId);

    public int getCartListCountForUser(int user);
    public void removeCartItem(int cartlistItem);

    boolean existsByUserAndProduct(User user, Product products);

    AddToCart findCart(User user, Product products);

    AddToCart updateCart(AddToCart cart);
}
