package com.Mirra.eCommerce.Service.ServiceImages;

import com.Mirra.eCommerce.DTO.BannerDto;
import com.Mirra.eCommerce.Models.ServiceImages.Banner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BannerService {
     void addBanner(BannerDto bannerDto) ;
    public Banner getExistingBanner();
}
