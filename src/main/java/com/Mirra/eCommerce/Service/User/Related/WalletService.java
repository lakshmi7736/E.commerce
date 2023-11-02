package com.Mirra.eCommerce.Service.User.Related;

import com.Mirra.eCommerce.Models.Users.Related.Wallet;
import com.Mirra.eCommerce.Models.Users.User;

public interface WalletService {
    public Wallet findByUser(User user);
    public Wallet save(Wallet wallet);
}
