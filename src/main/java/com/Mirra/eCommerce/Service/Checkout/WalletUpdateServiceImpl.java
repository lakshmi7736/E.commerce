package com.Mirra.eCommerce.Service.Checkout;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Users.Related.Wallet;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Repository.User.Related.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletUpdateServiceImpl implements WalletUpadteService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public void handleWallet(Order order, User user, BigDecimal grandTotaled, BigDecimal amount) {
        if (amount != null) {
            order.setPurchaseTotal(grandTotaled.subtract(amount));
            Wallet wallet = walletRepository.findByUser(user);

            if (wallet != null) {
                // Update the wallet balance with the purchase amount
                BigDecimal currentBalance = wallet.getAmount();
                BigDecimal newBalance = currentBalance.subtract(grandTotaled);

                // Ensure the balance doesn't go below zero
                if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                    newBalance = BigDecimal.ZERO;
                }
                order.setRedeemedFromWallet(true);
                order.setRedeemedAmount(amount);
                wallet.setAmount(newBalance);

                // Save the updated wallet
                walletRepository.save(wallet);
            }
        }
    }
}
