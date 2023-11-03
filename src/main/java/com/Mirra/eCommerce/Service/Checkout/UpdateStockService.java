package com.Mirra.eCommerce.Service.Checkout;

import com.Mirra.eCommerce.Models.Users.Related.AddToCart;

import java.util.List;

public interface UpdateStockService {

    public void updateProductStock(List<AddToCart> cartItems);
}
