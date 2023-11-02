package com.Mirra.eCommerce.Service.Coupons;

import com.Mirra.eCommerce.DTO.Coupons.ReferralPointsDto;
import com.Mirra.eCommerce.Models.Coupons.ReferralPoints;
import com.Mirra.eCommerce.Models.ServiceImages.Banner;
import com.Mirra.eCommerce.Repository.Coupons.ReferralPointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferralPointsServiceImpl implements ReferralPointsService{

    @Autowired
    private ReferralPointsRepository referralPointsRepo;
    @Override
    public void savePoints(ReferralPointsDto pointsDto) {
        ReferralPoints existingPoints=referralPointsRepo.findAnyReferralPoints();
        if (existingPoints!=null){
            referralPointsRepo.delete(existingPoints);
        }

        ReferralPoints points=new ReferralPoints();
        points.setId(pointsDto.getId());
        points.setReferralBonus(pointsDto.getReferralBonus());
        points.setActivatedBonus(pointsDto.getActivatedBonus());
        referralPointsRepo.save(points);
    }

    @Override
    public ReferralPoints findAnyReferralPoints() {
        return referralPointsRepo.findAnyReferralPoints();
    }
}
