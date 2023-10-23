package com.Mirra.eCommerce.Service.Token;

import com.Mirra.eCommerce.Models.Token.RefreshToken;

public interface RefreshTokenService {
    public RefreshToken createRefreshToken(String userName);

    public RefreshToken verifyRefreshToken(String refreshToken);
}
