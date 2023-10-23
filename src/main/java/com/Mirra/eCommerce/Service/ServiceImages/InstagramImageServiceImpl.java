package com.Mirra.eCommerce.Service.ServiceImages;

import com.Mirra.eCommerce.Models.ServiceImages.Instagram;
import com.Mirra.eCommerce.Repository.ServiceImages.InstagramRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstagramImageServiceImpl implements InstagramImageService{

    @Autowired
    private InstagramRepo instagramRepo;

}
