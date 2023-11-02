package com.Mirra.eCommerce.Service.User.Related;

import com.Mirra.eCommerce.Models.Users.Related.Wallet;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Repository.User.Related.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletRepository walletRepo;

    @Override
    public Wallet findByUser(User user) {
        return walletRepo.findByUser(user);
    }

    @Override
    public Wallet save(Wallet wallet) {
        return walletRepo.save(wallet);
    }
}
