package com.Mirra.eCommerce.Service.ServiceImages;


import com.Mirra.eCommerce.DTO.InstagramDto;
import com.Mirra.eCommerce.Models.ServiceImages.Instagram;

import java.util.List;

public interface InstagramImageService {

    void addInstagram(InstagramDto instagramDto);

    List<Instagram> findAll();

    void deleteById(int id);


}
