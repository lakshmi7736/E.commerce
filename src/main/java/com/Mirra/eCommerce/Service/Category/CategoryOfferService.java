package com.Mirra.eCommerce.Service.Category;

import com.Mirra.eCommerce.DTO.CategoryOfferDto;
import com.Mirra.eCommerce.Models.datas.CategoryOffer;

import java.util.List;

public interface CategoryOfferService {

    public void saveCategoryOffer(CategoryOfferDto categoryOfferDto) ;

    public void saveCategoryOffer(Long categoryId,CategoryOfferDto categoryOfferDto) ;

    public CategoryOffer findByCategoryId(Long categoryId);

    List<CategoryOffer> findAll();

    void deleteByCategoryOfferId(int categoryOfferId);

    CategoryOffer findById(int id);

}
