package com.Mirra.eCommerce.Service.Coupons;

import com.Mirra.eCommerce.DTO.Coupons.ReferralDto;
import com.Mirra.eCommerce.Models.Coupons.Referral;
import com.Mirra.eCommerce.Models.Users.User;

public interface ReferralService {

    void saveReferral(ReferralDto referralDto);

    public boolean codeExistsByName(String code);

    public boolean isCodeValid(String code);

    public boolean userExist(int userId);

    public User findUserByCode(String code);

    Referral findCodeByUser(User user);
}
