package com.Mirra.eCommerce.Service.Coupons;

import com.Mirra.eCommerce.DTO.Coupons.ReferralPointsDto;
import com.Mirra.eCommerce.Models.Coupons.ReferralPoints;

public interface ReferralPointsService {

    void savePoints(ReferralPointsDto pointsDto);

    ReferralPoints findAnyReferralPoints();
}
