package com.Mirra.eCommerce.Repository.Token;

import com.Mirra.eCommerce.Models.Token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByRefreshToken(String token);
}