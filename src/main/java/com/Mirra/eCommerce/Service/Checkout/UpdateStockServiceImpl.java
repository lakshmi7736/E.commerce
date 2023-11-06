package com.Mirra.eCommerce.Service.Checkout;

import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Repository.Datas.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateStockServiceImpl implements UpdateStockService {


    @Autowired
    private ProductRepository productRepository;

    @Override
    public void updateProductStock(List<AddToCart> cartItems) {
        for (AddToCart cartItem : cartItems) {
            Product product = cartItem.getProducts();
            int quantityInCart = cartItem.getQuantity();
            int updatedStock = product.getStock() - quantityInCart;

            if (updatedStock > 0) {
                // Only update the stock if it won't go negative
                product.setStock(updatedStock);
                productRepository.save(product); // Save the updated product
            } else {
                product.setActive(false);
                // Only update the stock if it won't go negative
                product.setStock(updatedStock);;
                productRepository.save(product); // Save the updated product
            }
        }
    }

    @Override
    public void updateProductStock(AddToCart cart) {
        Product product = cart.getProducts();
        int quantityInCart = cart.getQuantity();
        int updatedStock = product.getStock() - quantityInCart;

        if (updatedStock > 0) {
            // Only update the stock if it won't go negative
            product.setStock(updatedStock);
            productRepository.save(product); // Save the updated product
        } else {
            product.setActive(false);
            // Only update the stock if it won't go negative
            product.setStock(updatedStock);;
            productRepository.save(product); // Save the updated product
        }
    }
}
