package com.Mirra.eCommerce.DTO.Coupons;

import com.Mirra.eCommerce.Models.Coupons.ReferralPoints;
import com.Mirra.eCommerce.Models.Users.User;
import lombok.Data;

@Data
public class ReferralDto {

    private int id;

    private String code;

    private ReferralPoints referralPointsId;

    private User userId;
}
