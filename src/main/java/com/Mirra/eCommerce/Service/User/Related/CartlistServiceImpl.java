package com.Mirra.eCommerce.Service.User.Related;

import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import com.Mirra.eCommerce.Models.Users.Related.WishList;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Repository.Datas.ProductRepository;
import com.Mirra.eCommerce.Repository.User.Related.CartlistReppository;
import com.Mirra.eCommerce.Repository.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartlistServiceImpl implements CartlistService{

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartlistReppository cartlistReppository;
    @Override
    public void addToCartlist(Long productId, String username) {
        AddToCart cartlist = new AddToCart();
        cartlist.setProducts(productRepository.findById(productId).get());
        cartlist.setUser(userRepo.findByEmail(username));
        cartlist.setQuantity(1);
        cartlistReppository.save(cartlist);
    }

    @Override
    public List<AddToCart> getCartListByUserId(int userId) {
        return cartlistReppository.findByUserId(userId);
    }

    @Override
    public int getCartListCountForUser(int user) {
        return cartlistReppository.countByUser_Id(user);
    }

    @Override
    public void removeCartItem(int cartlistItem) {
        cartlistReppository.deleteById(cartlistItem);
    }

    @Override
    public boolean existsByUserAndProduct(User user, Product products) {
        return cartlistReppository.existsByUserAndProducts(user,products);
    }

    @Override
    public AddToCart findCart(User user, Product products) {
        return cartlistReppository.findCartByUserAndProducts(user,products);
    }

    @Override
    public AddToCart updateCart(AddToCart cart) {
        return  cartlistReppository.save(cart);
    }


    @Override
    public void clearCartByUser(User user) {
        cartlistReppository.deleteCartByUser(user);
    }

    @Override
    public AddToCart findById(int cartId) {
        return cartlistReppository.findById(cartId).get();
    }
}
