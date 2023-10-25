package com.Mirra.eCommerce.Service.ServiceImages;

import com.Mirra.eCommerce.DTO.InstagramDto;
import com.Mirra.eCommerce.Models.ServiceImages.Instagram;
import com.Mirra.eCommerce.Repository.ServiceImages.InstagramRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class InstagramImageServiceImpl implements InstagramImageService{

    @Autowired
    private InstagramRepo instagramRepo;

    @Override
    public void addInstagram(InstagramDto instagramDto) {
        byte[] imageData = null;
        if (instagramDto.getImageData() != null) {
            imageData = Base64.getDecoder().decode(instagramDto.getImageData());
        }
        if(instagramDto!=null){
            System.out.println("tadada "+instagramDto.getId());
            Instagram instagram=Instagram.builder()
                    .id(instagramDto.getId())
                    .instagramId(instagramDto.getInstagramId())
                    .url(instagramDto.getUrl())
                    .image(imageData)
                    .build();
            instagramRepo.save(instagram);
        }

    }

    @Override
    public List<Instagram> findAll() {
        return instagramRepo.findAll();
    }

    @Override
    public void deleteById(int id) {
        instagramRepo.deleteById(id);
    }
}
