package com.Mirra.eCommerce.Service.Checkout;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Users.User;

import java.math.BigDecimal;

public interface WalletUpadteService {

    public void handleWallet(Order order, User user, BigDecimal grandTotaled, BigDecimal amount);
}
