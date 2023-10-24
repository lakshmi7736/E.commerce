package com.Mirra.eCommerce.DTO;


import com.Mirra.eCommerce.Models.datas.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerDto {

    private int id;

    private String collection;

    private String saleEvent;

    private String imageData;

    private Category categoryId;
}
