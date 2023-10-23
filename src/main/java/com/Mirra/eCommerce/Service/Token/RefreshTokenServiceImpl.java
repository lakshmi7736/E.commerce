package com.Mirra.eCommerce.Service.Token;

import com.Mirra.eCommerce.Models.Token.RefreshToken;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Repository.Token.RefreshTokenRepository;
import com.Mirra.eCommerce.Repository.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{


        public long refreshTokenValidity=5*60*60*10000;
        @Autowired
        private RefreshTokenRepository refreshTokenRepository;

        @Autowired
        private UserRepo userRepository;

        @Override
        public RefreshToken createRefreshToken(String userName){

            User user = userRepository.findByEmail(userName);
            System.out.println(user);

            RefreshToken refreshToken1=user.getRefreshToken();

            if(refreshToken1==null){

                refreshToken1=RefreshToken.builder()
                        .refreshToken(UUID.randomUUID().toString())
                        .expiry(Instant.now().plusMillis(refreshTokenValidity))
                        .user(user)
                        .build();
            }else {
                refreshToken1.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
            }

            user.setRefreshToken(refreshToken1);


            //save to database
            refreshTokenRepository.save(refreshToken1);
            return refreshToken1;
        }

        @Override
        public RefreshToken verifyRefreshToken(String refreshToken){

            RefreshToken refreshTokenOb=refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new RuntimeException("TOKEN DOES NOT EXIST IN YOUR DATABASE !!"));
            if(refreshTokenOb.getExpiry().compareTo(Instant.now())<0){
                refreshTokenRepository.delete(refreshTokenOb);
                throw  new RuntimeException("Refresh Token Expired");
            }
            return refreshTokenOb;
        }

}
