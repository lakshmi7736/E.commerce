package com.Mirra.eCommerce.Service.ServiceImages;

import com.Mirra.eCommerce.DTO.BannerDto;
import com.Mirra.eCommerce.DTO.CategoryDto;
import com.Mirra.eCommerce.Models.ServiceImages.Banner;
import com.Mirra.eCommerce.Models.datas.Category;
import com.Mirra.eCommerce.Repository.Datas.CategoryRepository;
import com.Mirra.eCommerce.Repository.ServiceImages.BannerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class BannerServiceImpl implements BannerService{

    @Autowired
    private BannerRepo bannerRepo;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public void addBanner(BannerDto bannerDto) {
        byte[] imageData = null;
        if (bannerDto.getImageData() != null) {
            imageData = Base64.getDecoder().decode(bannerDto.getImageData());
        }

        Banner existingbanner=bannerRepo.findAnyBanner();
        if (existingbanner!=null){
            bannerRepo.delete(existingbanner);
        }

        Banner banner = Banner.builder()
                .id(bannerDto.getId())
                .collection(bannerDto.getCollection())
                .saleEvent(bannerDto.getSaleEvent())
                .category(bannerDto.getCategoryId()) // Set the Category object, not categoryId
                .image(imageData)
                .build();
        bannerRepo.save(banner);
    }


    @Override
    public Banner getExistingBanner() {
        return bannerRepo.findAnyBanner();
    }
}
