package com.Mirra.eCommerce.Service.Coupons;

import com.Mirra.eCommerce.DTO.Coupons.ReferralDto;
import com.Mirra.eCommerce.Models.Coupons.Referral;
import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Repository.Coupons.ReferralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferralServiceImpl implements ReferralService{


    @Autowired
    private ReferralRepository referralRepo;

    @Override
    public void saveReferral(ReferralDto referralDto) {
        Referral referral=new Referral();
        referral.setId(referralDto.getId());
        referral.setUser(referralDto.getUserId());
        referral.setCode(referralDto.getCode());
        referral.setReferralPoints(referralDto.getReferralPointsId());
        referralRepo.save(referral);
    }

    @Override
    public boolean codeExistsByName(String code) {
        return referralRepo.existsByCode(code);
    }

    @Override
    public boolean isCodeValid(String code) {
        // Check if the code is not blank and is alphanumeric
        if (code != null && !code.isBlank() && code.matches("^[a-zA-Z0-9]*$")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean userExist(int userId){
        return referralRepo.existsByUserId(userId);
    }

    @Override
    public User findUserByCode(String code) {
        Referral referral = referralRepo.findByCode(code);
        if (referral != null) {
            // Assuming Referral entity has a 'user' field representing the associated User.
            return referral.getUser();
        } else {
            return null; // Return null if no user is found with the given code.
        }
    }

    @Override
    public Referral findCodeByUser(User user) {
        return referralRepo.findCodeByUser(user);
    }


}


